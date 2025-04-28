using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace TecnoVentasAPI.Models
{
    [Table("cliente")]
    public class Cliente
    {
        [Key]
        [Column("id_cliente")]
        public int IdCliente { get; set; }
        [Required]
        [Column("id_usuario")]
        public int IdUsuario { get; set; }
        [Required]
        [Column("nombre")]
        public string? Nombre {  get; set; }
        [Required]
        [Column("apellido")]
        public string? Apellido { get; set; }
        [Column("direccion")]
        public string? Direccion { get; set; }
        [Column("telefono")]
        public string? Telefono { get; set; }
        [Column("correo")]
        public string? Correo { get; set; }
        [Column("nit")]
        public int Nit { get; set; }

        // public ICollection<Factura>? Facturas { get; set; }  //Relacion uno a muchos con Factura
    }
}
