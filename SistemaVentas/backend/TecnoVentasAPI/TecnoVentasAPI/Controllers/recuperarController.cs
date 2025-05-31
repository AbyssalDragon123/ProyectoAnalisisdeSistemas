using System.Net.Mail;
using System.Net;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using TecnoVentasAPI.Data;
using TecnoVentasAPI.DTOs;
using TecnoVentasAPI.Models;

namespace TecnoVentasAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class RecuperarController : ControllerBase
    {
        private readonly AppDbContext _context;

        public RecuperarController (AppDbContext context)
        {
            _context = context;
        }

        // POST: api/recuperacion/forgot-password
        [HttpPost("forgot-password")]
        public async Task<IActionResult> ForgotPassword([FromBody] RecuperarContrasenaDto dto)
        {
            var usuario = await _context.Usuarios.FirstOrDefaultAsync(u => u.Correo == dto.Correo);
            if (usuario == null)
                return NotFound(new { message = "Correo no registrado" });

            string token = Guid.NewGuid().ToString();
            usuario.PasswordResetToken = token;
            usuario.PasswordResetExpires = DateTime.Now.AddHours(1);

            await _context.SaveChangesAsync();

            var enviado = await EnviarCorreoRecuperacion(dto.Correo, token);
            if (!enviado)
                return StatusCode(500, new { message = "Error al enviar el correo." });

            return Ok(new { message = "Se envió el correo con el token de recuperación." });

        }

        // POST: api/recuperacion/reset-password
        [HttpPost("reset-password")]
        public async Task<IActionResult> ResetPassword([FromBody] RecuperarContrasenaDto dto)
        {
            var usuario = await _context.Usuarios.FirstOrDefaultAsync(u =>
                u.Username == dto.Username &&
                u.PasswordResetToken == dto.Token &&
                u.PasswordResetExpires > DateTime.Now);

            if (usuario == null)
                return BadRequest(new { message = "Token inválido o expirado" });

            usuario.Pass = dto.NuevaContrasena;
            usuario.PasswordResetToken = null;
            usuario.PasswordResetExpires = null;

            await _context.SaveChangesAsync();

            return Ok(new { message = "Contraseña actualizada correctamente" });
        }

        //metodo para enviar por correo el token de recuparación
        private async Task<bool> EnviarCorreoRecuperacion(string correoDestino, string token)
        {
            try
            {
                var remitente = "pruebasunregional@gmail.com"; // correo remitente con pass App
                var contraseña = "grmdgfbqomerobkb";

                var asunto = "Recuperación de contraseña";
                var mensaje = $"Atentamente confirmamos su solicitud realizada para la recuperación de su contraseña,\n\n" +
                    $"Este es tu código de recuperación: {token}\n\nEste código expirará en 15 minutos.";

                var smtp = new SmtpClient("smtp.gmail.com")
                {
                    Port = 587,
                    Credentials = new NetworkCredential(remitente, contraseña),
                    EnableSsl = true,
                };

                var mail = new MailMessage(remitente, correoDestino, asunto, mensaje);
                await smtp.SendMailAsync(mail);

                return true;
            }
            catch
            {
                return false;
            }
        }

    }


}


