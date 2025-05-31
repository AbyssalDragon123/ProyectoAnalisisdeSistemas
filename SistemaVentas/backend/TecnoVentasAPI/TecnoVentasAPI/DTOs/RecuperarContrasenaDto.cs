using System.ComponentModel.DataAnnotations;

public class RecuperarContrasenaDto
{
    public string Username { get; set; }
    public string NuevaContrasena { get; set; }
    public string Token { get; set; }
    [EmailAddress(ErrorMessage = "El correo no tiene un formato válido")]
    public string Correo { get; set; }
}
