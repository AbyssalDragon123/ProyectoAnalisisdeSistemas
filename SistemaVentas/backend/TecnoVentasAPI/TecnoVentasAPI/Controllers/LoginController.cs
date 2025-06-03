using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Options;
using Microsoft.IdentityModel.Tokens;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;
using TecnoVentasAPI.Data;
using TecnoVentasAPI.DTOs;
using TecnoVentasAPI.Util; // Para acceder a JwtSettings

namespace TecnoVentasAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class LoginController : ControllerBase
    {
        private readonly AppDbContext _context;
        private readonly JwtSettings _jwtSettings;

        // Inyectamos el contexto y la configuración JWT desde el appsettings
        public LoginController(AppDbContext context, IOptions<JwtSettings> jwtSettings)
        {
            _context = context;
            _jwtSettings = jwtSettings.Value;
        }

        [HttpPost("login")]
        public IActionResult Login([FromBody] LoginRequest loginRequest)
        {
            // Validación básica del cuerpo de la petición
            if (loginRequest == null || string.IsNullOrEmpty(loginRequest.UserName) || string.IsNullOrEmpty(loginRequest.Password))
            {
                return BadRequest("Faltan datos para la validación...");
            }

            // Verificar que el usuario y contraseña existen en la base de datos
            var user = _context.Usuarios.FirstOrDefault(u =>
                u.Username == loginRequest.UserName && u.Pass == loginRequest.Password);

            if (user == null)
            {
                return Unauthorized("Usuario o Contraseña incorrectos.");
            }

            // Crear los 'claims' que se incluirán en el token
            var claims = new[]
            {
                new Claim(ClaimTypes.Name, user.Username),
                new Claim(ClaimTypes.Role, user.Rol),
                new Claim(JwtRegisteredClaimNames.Jti, Guid.NewGuid().ToString()) // ID único del token
            };

            // Crear la clave de seguridad a partir del secret
            var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_jwtSettings.Key));
            var credentials = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);

            // Generar el token JWT
            var token = new JwtSecurityToken(
                issuer: _jwtSettings.Issuer,
                audience: _jwtSettings.Audience,
                claims: claims,
                expires: DateTime.UtcNow.AddMinutes(_jwtSettings.ExpireMinutes),
                signingCredentials: credentials
            );

            // Retornar el token al cliente
            return Ok(new
            {
                token = new JwtSecurityTokenHandler().WriteToken(token),
                expiration = token.ValidTo
            });
        }
    }
}
