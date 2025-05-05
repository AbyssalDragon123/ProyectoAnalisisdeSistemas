using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace TecnoVentasAPI.Models
{
    [Table("categoria")]
    public class Categoria
    {
        [Key]
        [Column("id_categoria")] //corregi de (idcategoria) a (id_categoria)
        public int IdCategoria { get; set; }
        [Required]
        [Column("nombre_cat")]
        public string NombreCat {  get; set; } = string.Empty;
        [Required]
        [Column("descripcion")]
        [StringLength(256)]
        public string Descripcion { get; set; } = string.Empty;

    }
}
