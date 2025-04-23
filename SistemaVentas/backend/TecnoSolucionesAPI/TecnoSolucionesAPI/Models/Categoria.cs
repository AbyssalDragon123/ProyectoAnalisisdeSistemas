using System.ComponentModel.DataAnnotations;

namespace TecnoSolucionesAPI.Models
{
    public class Categoria
    {

        [Key]
        public int IdCategoria { get; set; }
        [Required]
        [MaxLength(150)]
        public string? Nombre { get; set; }

        [MaxLength(256)]
        public string? Descripcion {  get; set; }
        
        public bool Estado { get; set; }

        public ICollection<Articulo>? Articulos { get; set; }
    }
}
