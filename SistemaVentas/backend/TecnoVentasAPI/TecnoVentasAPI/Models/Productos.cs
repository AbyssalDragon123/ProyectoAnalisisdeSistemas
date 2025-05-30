using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

[Table("producto")]
public class Producto
{
    [Key]
    [Column("id_producto")]
    public int IdProducto { get; set; }

    [Column("id_usuario")]
    public int IdUsuario { get; set; }

    [Column("id_categoria")]
    public int IdCategoria { get; set; }

    [Column("nombre")]
    [StringLength(100)]
    public string? Nombre { get; set; }

    [Column("precio_venta", TypeName = "decimal(10,2)")]
    public decimal? PrecioVenta { get; set; }

    [Column("stock")]
    public int? Stock { get; set; }

    [Column("fecha_creacion")]
    public DateTime FechaCreacion { get; set; } = DateTime.Now;

    // Relaciones (opcional, si tienes clases Usuario y Categoria)
    // public virtual Usuario Usuario { get; set; }
    // public virtual Categoria Categoria { get; set; }
}