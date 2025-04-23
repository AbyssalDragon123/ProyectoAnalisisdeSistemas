using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;


namespace TecnoSolucionesAPI.Models
{
    public class Articulo
    {
        [Key]
        public int IdArticulo { get; set; }

        public int IdCaterogia { get; set; }
        public Categoria Categoria { get; set; }

        public int IdMarca { get; set; }
        public Marca Marca { get; set; }

        public string? Codigo { get; set; }
        [Required]
        [MaxLength(100)]
        public string? Nombre { get; set; }
        [Required]
        public decimal PrecioVenta { get; set; }
        [Required]
        public int Stock { get; set; }
        [MaxLength(256)]
        public string? Descripcion { get; set; } //declara ? acepta valores null

        public bool Estado { get; set; } = true;


    }

}
