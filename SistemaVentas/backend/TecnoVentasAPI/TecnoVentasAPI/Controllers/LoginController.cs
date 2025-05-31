using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using TecnoVentasAPI.Data;
using TecnoVentasAPI.DTOs;

namespace TecnoVentasAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class LoginController : ControllerBase
    {
        private readonly AppDbContext _context;
        public LoginController(AppDbContext context)
        {
            _context = context;
        }

        [HttpPost("login")]

        public IActionResult Login([FromBody] LoginRequest loginRequest)
        {
            if (loginRequest == null || string.IsNullOrEmpty(loginRequest.UserName) || string.IsNullOrEmpty(loginRequest.Password))
            {
                return BadRequest("Faltan datos para la validación...");
            }
            var user = _context.Usuarios.FirstOrDefault(u => u.Username == loginRequest.UserName && u.Pass == loginRequest.Password);
            if (user == null)
            {
                return Unauthorized("Usuario o Contraseña incorrectos.");
            }
            // Aquí puedes generar un token JWT y devolverlo al cliente
            // Por simplicidad, solo devolvemos el nombre de usuario
            return Ok(new { user.Username, user.Rol });
        }
    }
}
