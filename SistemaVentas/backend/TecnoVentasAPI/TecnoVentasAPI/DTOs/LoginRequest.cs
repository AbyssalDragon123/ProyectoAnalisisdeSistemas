using System.ComponentModel.DataAnnotations.Schema;


namespace TecnoVentasAPI.DTOs
{
    public class LoginRequest
    {
        // Nombre de usuario o email con el que el usuario se autentica
        public string? UserName { get; set; }

        // Contraseña que se validará con la base de datos
        public string? Password { get; set; }
        public string? Rol { get; set; }
    }
}
