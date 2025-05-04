using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace TecnoVentasAPI.Models
{
    [Table("producto", Schema = "tecno_ventas")]
    public class Producto
    {
        [Key]
        [Column("id_producto")]
        public int IdProducto { get; set; }

        [Required]
        [Column("id_usuario")]
        public int IdUsuario { get; set; }

        [Required]
        [Column("id_categoria")]
        public int IdCategoria { get; set; }

        [Column("nombre")]
        [MaxLength(100)]
        public string? Nombre { get; set; }

        [Column("precio_venta", TypeName = "decimal(10,2)")]
        public decimal? PrecioVenta { get; set; }

        [Column("stock")]
        public int? Stock { get; set; }

       [ForeignKey("IdUsuario")]
        public Usuario Usuario { get; set; } = null!;

        [ForeignKey("IdCategoria")]
        public Categoria Categoria { get; set; } = null!;
    }
}
