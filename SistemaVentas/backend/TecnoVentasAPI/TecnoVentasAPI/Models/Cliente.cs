using System.ComponentModel.DataAnnotations;

namespace TecnoVentasAPI.Models
{
    public class Cliente
    {
        [Key]
        public int IdCliente { get; set; }
        [Required]
        public string? Nombre {  get; set; }
        [Required]
        public string? NombreApellido { get; set; }

        public ICollection<Factura>? Facturas { get; set; }
    }
}
