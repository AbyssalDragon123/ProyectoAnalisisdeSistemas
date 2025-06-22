namespace TecnoVentasAPI.DTOs
{
    public class FacturaDetalleDTO
    {
        public int IdFactura { get; set; }
        public DateTime? FechaFactura { get; set; }

        public string ClienteNombre { get; set; } = string.Empty;
        public string UsuarioNombre { get; set; } = string.Empty;

        public List<DetalleDTO> Detalles { get; set; } = new();
    }

    public class DetalleDTO
    {
        public string ProductoNombre { get; set; } = string.Empty;
        public int Cantidad { get; set; }
        public decimal PrecioUnitario { get; set; }
    }
}
