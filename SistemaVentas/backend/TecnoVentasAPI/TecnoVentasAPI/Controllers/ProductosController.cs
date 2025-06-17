using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;
using TecnoVentasAPI.Data;
using TecnoVentasAPI.Models;
using MySqlConnector; // Necesario para MySqlException

namespace TecnoVentasAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    [Authorize] // Requiere autenticación para todas las acciones del controlador
    public class ProductosController : ControllerBase
    {
        private readonly AppDbContext _context;
        private readonly ILogger<ProductosController> _logger; // Campo para el logger

        // Constructor: Inyección de dependencias de AppDbContext y ILogger
        public ProductosController(AppDbContext context, ILogger<ProductosController> logger)
        {
            _context = context;
            _logger = logger; // Asigna la instancia del logger
        }

        // GET: api/Productos
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Producto>>> GetProductos()
        {
            if (_context.Productos == null) return NotFound();
            return await _context.Productos.ToListAsync();
        }

        // GET: api/Productos/5
        [HttpGet("{id}")]
        public async Task<ActionResult<Producto>> GetProducto(int id)
        {
            if (_context.Productos == null) return NotFound();
            var producto = await _context.Productos.FindAsync(id);
            if (producto == null) return NotFound();
            return producto;
        }

        // PUT: api/Productos/5
        [HttpPut("{id}")]
        public async Task<IActionResult> PutProducto(int id, Producto producto)
        {
            if (id != producto.IdProducto) return BadRequest();
            _context.Entry(producto).State = EntityState.Modified;
            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!ProductoExists(id)) return NotFound();
                else throw;
            }
            return NoContent();
        }

        // POST: api/Productos
        [HttpPost]
        public async Task<ActionResult<Producto>> PostProducto(Producto producto)
        {
            if (_context.Productos == null) return Problem("Entity set 'AppDbContext.Productos' is null.");
            _context.Productos.Add(producto);
            await _context.SaveChangesAsync();
            return CreatedAtAction("GetProducto", new { id = producto.IdProducto }, producto);
        }

        // DELETE: api/Productos/5
        // Maneja la eliminación y la restricción de clave foránea.
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteProducto(int id)
        {
            // Log de depuración para verificar la cabecera de autorización.
            if (Request.Headers.ContainsKey("Authorization"))
            {
                _logger.LogInformation($"Auth header for DELETE product ID {id} received.");
            }

            var producto = await _context.Productos.FindAsync(id);
            if (producto == null) return NotFound();

            try
            {
                _context.Productos.Remove(producto);
                await _context.SaveChangesAsync();
                _logger.LogInformation($"Product ID {id} deleted successfully.");
                return NoContent(); // Éxito: 204 No Content
            }
            catch (DbUpdateException dbEx) // Captura excepciones de Entity Framework al guardar cambios
            {
                // Si la excepción interna es de MySQL y es una falla de clave foránea
                if (dbEx.InnerException is MySqlException mysqlEx && mysqlEx.Message.Contains("Cannot delete or update a parent row: a foreign key constraint fails"))
                {
                    _logger.LogWarning(dbEx, $"Delete of product ID {id} failed due to foreign key constraint.");
                    // Devuelve 409 Conflict con mensaje amigable
                    return StatusCode(409, "No se puede eliminar el producto porque está asociado a uno o más detalles de venta.");
                }
                else
                {
                    // Otros errores de base de datos
                    _logger.LogError(dbEx, $"Unexpected DB error deleting product ID {id}.");
                    return StatusCode(500, "Error de base de datos al eliminar el producto.");
                }
            }
            catch (Exception ex) // Captura cualquier otra excepción inesperada
            {
                _logger.LogError(ex, $"Unexpected error deleting product ID {id}.");
                return StatusCode(500, "Error interno del servidor al eliminar el producto.");
            }
        }

        // Método auxiliar para verificar si un producto existe.
        private bool ProductoExists(int id)
        {
            return (_context.Productos?.Any(e => e.IdProducto == id)).GetValueOrDefault();
        }
    }
}