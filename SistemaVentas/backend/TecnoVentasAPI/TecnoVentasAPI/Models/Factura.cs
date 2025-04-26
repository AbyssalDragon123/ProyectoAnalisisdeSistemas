using System;
using System.ComponentModel.DataAnnotations;

namespace TecnoVentasAPI.Models
{
    public class Factura
    {
        [Key]
        public int IdFactura { get; set; }

        public DateOnly Fecha_Factura { get; set; }
        public int IdCliente { get; set; }
        public Cliente? Cliente { get; set; }

        public ICollection<DetalleFactura>? DetalleFacturas { get; set; }
    }
}
