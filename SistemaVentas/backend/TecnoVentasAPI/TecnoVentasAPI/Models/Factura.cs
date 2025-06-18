using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace TecnoVentasAPI.Models
{
    [Table("factura", Schema = "tecno_ventas")]
    public class Factura
    {
        [Key]
        [Column("id_factura")]
        public int IdFactura { get; set; }

        [Column("fecha_factura")]
        public DateTime? FechaFactura { get; set; }

        [Column("id_cliente")]
        public int? IdCliente { get; set; }

        [Required]
        [Column("id_usuario")]
        public int IdUsuario { get; set; }
        public ICollection<DetalleVenta> DetalleVentas { get; set; } = new List<DetalleVenta>();


        /*[ForeignKey("IdCliente")]
         public Cliente? Cliente { get; set; }

         [ForeignKey("IdUsuario")]  //relación uno a muchos con Usuario
         public Usuario Usuario { get; set; } = null!;*/
    }
}
