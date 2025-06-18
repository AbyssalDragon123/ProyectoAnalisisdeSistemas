using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using TecnoVentasAPI.Data;
using TecnoVentasAPI.DTOs;
using TecnoVentasAPI.Models;

namespace TecnoVentasAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class GestionFacturaController : ControllerBase
    {
        private readonly AppDbContext _context;

        public GestionFacturaController(AppDbContext context)
        {
            _context = context;
        }

        [HttpGet("completas")]
        public async Task<ActionResult<IEnumerable<FacturaDetalleDTO>>> ObtenerFacturasCompletas()
        {
            var facturas = await _context.Facturas
                .Include(f => f.DetalleVentas)
                .ToListAsync();

            var resultado = new List<FacturaDetalleDTO>();

            foreach (var f in facturas)
            {
                var cliente = await _context.Clientes.FindAsync(f.IdCliente);
                var usuario = await _context.Usuarios.FindAsync(f.IdUsuario);

                var detallesDTO = new List<DetalleDTO>();

                foreach (var detalle in f.DetalleVentas)
                {
                    var producto = await _context.Productos.FindAsync(detalle.IdProducto);

                    detallesDTO.Add(new DetalleDTO
                    {
                        ProductoNombre = producto?.Nombre ?? "Producto no encontrado",
                        Cantidad = (int)detalle.Cantidad,
                        PrecioUnitario = (decimal)detalle.PrecioVenta
                    });
                }

                resultado.Add(new FacturaDetalleDTO
                {
                    IdFactura = f.IdFactura,
                    FechaFactura = f.FechaFactura,
                    ClienteNombre = cliente?.Nombre ?? "Desconocido",
                    UsuarioNombre = usuario?.Username ?? "Desconocido",
                    Detalles = detallesDTO
                });
            }

            return Ok(resultado);
        }
    }
}
