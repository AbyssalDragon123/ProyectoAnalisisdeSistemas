using BCrypt.Net;

public static class SeguridadUtil
{
    // Hashear la contraseña
    public static string HashearPassword(string password)
    {
        return BCrypt.Net.BCrypt.HashPassword(password);
    }

    // Verificar contraseña ingresada con la contraseña hasheada almacenada
    public static bool VerificarPassword(string passwordIngresada, string hashGuardado)
    {
        return BCrypt.Net.BCrypt.Verify(passwordIngresada, hashGuardado);
    }
}
