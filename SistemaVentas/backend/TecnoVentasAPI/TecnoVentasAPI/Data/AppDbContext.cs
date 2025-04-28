using TecnoVentasAPI.Models;
using Microsoft.EntityFrameworkCore;

namespace TecnoVentasAPI.Data
{
    public class AppDbContext : DbContext

    {
        public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) // conectar la base de datos con el appsettings json
        {
        }
        public DbSet<Cliente> Clientes { get; set; }
        
    }
}
