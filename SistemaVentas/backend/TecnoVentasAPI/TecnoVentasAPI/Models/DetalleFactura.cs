using System.ComponentModel.DataAnnotations;

namespace TecnoVentasAPI.Models
{
    public class DetalleFactura
    {
        [Key]
        public int IdDetalle { get; set; }
        public int IdFactura { get; set; }
        public Factura? Factura { get; set; }
        public int IdProducto {  get; set; }
        public Producto? Producto { get; set; }
        public int Cantidad {  get; set; }
        public decimal Precio_Venta {  get; set; }

    }
}
