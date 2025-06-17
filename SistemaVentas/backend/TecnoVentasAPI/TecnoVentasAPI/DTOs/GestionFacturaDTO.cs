namespace TecnoVentasAPI.DTOs
{
    public class GestionFactura
    {
        public int IdFactura { get; set; }
        public DateTime FechaFactura { get; set; }
        public string NombreCliente { get; set; } = string.Empty;
        public string NombreUsuario { get; set; } = string.Empty;
    }
}