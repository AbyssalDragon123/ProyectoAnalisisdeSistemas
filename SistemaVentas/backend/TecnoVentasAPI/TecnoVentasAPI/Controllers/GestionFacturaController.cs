using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using TecnoVentasAPI.Data;
using TecnoVentasAPI.DTOs;

namespace TecnoVentasAPI.Controllers
{/*
    [ApiController]
    [Route("api/[controller]")]
    public class GestionFacturaController : ControllerBase
    {
        private readonly AppDbContext _context;

        public GestionFacturaController(AppDbContext context)
        {
            _context = context;
        }

        [HttpGet("con-nombres")]
        public async Task<ActionResult<IEnumerable<GestionFactura>>> GetFacturasConNombres()
        {
            var facturas = await _context.Facturas
                .Include(f => f.Cliente)
                .Include(f => f.Usuario)
                .Select(f => new GestionFactura
                {
                    IdFactura = f.IdFactura,
                    FechaFactura = f.FechaFactura,
                    NombreCliente = f.Cliente.Nombre,
                    NombreUsuario = f.Usuario.Nombre
                }).ToListAsync();

            return Ok(facturas);
        }
    }*/
}