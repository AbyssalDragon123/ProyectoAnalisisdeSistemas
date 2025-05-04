using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace TecnoVentasAPI.Models
{
    [Table("Cliente")]
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
        public string Nombre { get; set; } = string.Empty;
        [Required]
        [Column("apellido")]
        public string Apellido { get; set; } = string.Empty;
        [Column("direccion")]
        public string? Direccion { get; set; }
        [Column("telefono")]
        public string? Telefono { get; set; }
        [Column("correo")]
        public string Correo { get; set; } = string.Empty;
        [Column("nit")]
        public string Nit { get; set; } = string.Empty;
        [Column("fecha_creacion")]
        public DateTime FechaCreacion { get; private set; } //fecha solo de lectura (se genera auto en la tabla)

        public ICollection<Factura>? Facturas { get; set; }  //Relacion uno a muchos con Factura 
        public ICollection<Usuario> Usuarios { get; set; }  // Relacion uno a muchos con Usuario
    }
}
