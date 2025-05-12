using System.ComponentModel.DataAnnotations.Schema;


namespace TecnoVentasAPI.DTOs
{
    
    public class LoginRequest
    {
        [Column("username")]
        public string? UserName { get; set; }
        [Column("pass")]
        public string? Password { get; set; }
        /*[Column("password_reset_token")]
        public string? Token { get; set; }
        [Column("password_reset_expires")]
        public DateOnly? ExpireToken { get; set; }
        public string? Email { get; set; }*/
        public string? rol { get; set; }
    }
}
