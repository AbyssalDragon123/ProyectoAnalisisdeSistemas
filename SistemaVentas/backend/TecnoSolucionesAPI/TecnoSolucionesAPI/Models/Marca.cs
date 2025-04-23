using System.ComponentModel.DataAnnotations;

namespace TecnoSolucionesAPI.Models
{
    public class Marca
    {
        [Key]
        public int IdMarca { get; set; }

        [Required]
        [MaxLength(50)]
        public string? Nombre { get; set; }

        public bool Estado { get; set; }

        public ICollection<Articulo>? Articulos {  get; set; }
    }
}
