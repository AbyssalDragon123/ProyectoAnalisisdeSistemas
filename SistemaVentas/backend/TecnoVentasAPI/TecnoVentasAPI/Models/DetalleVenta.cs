using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace TecnoVentasAPI.Models
{
    [Table("detalle_venta", Schema = "tecno_ventas")]
    public class DetalleVenta
    {
        [Key]
        [Column("id_detalle")]
        public int IdDetalle { get; set; }

        [Required]
        [Column("id_factura")]
        public int IdFactura { get; set; }

        [Required]
        [Column("id_producto")]
        public int IdProducto { get; set; }

        [Column("cantidad")]
        public int? Cantidad { get; set; }

        [Column("precio_venta", TypeName = "decimal(10,2)")]
        public decimal? PrecioVenta { get; set; }

        [ForeignKey("IdFactura")]
        public Factura Factura { get; set; } = null!;

        [ForeignKey("IdProducto")]
        public Producto Producto { get; set; } = null!;
    }
}
