using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace TecnoVentasAPI.Models
{
    public class Cliente
    {
        [Key]
        [Column("id_cliente")]
        public int IdCliente { get; set; }
        [Required]
        [Column("nombre")]
        public string? Nombre {  get; set; }
        [Required]
        [Column("apellido")]
        public string? Apellido { get; set; }

       // public ICollection<Factura>? Facturas { get; set; }
    }
}
