using System.ComponentModel.DataAnnotations;

namespace TecnoVentasAPI.Models
{
    public class Producto
    {
        [Key]
        public int IdProducto { get; set; }
        [Required]
        public string? Nombre { get; set; }
        public decimal PrecioVenta { get; set; }
        public int Stock { get; set; }

        public ICollection<DetalleFactura>? DetalleFacturas { get; set; }
    }
}
