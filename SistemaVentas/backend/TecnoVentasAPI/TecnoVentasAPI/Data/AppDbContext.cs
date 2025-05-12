using TecnoVentasAPI.Models;
using Microsoft.EntityFrameworkCore;
using TecnoVentasAPI.DTOs;

namespace TecnoVentasAPI.Data
{
    public class AppDbContext : DbContext

    {
        public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) // conectar la base de datos con el appsettings json
        {
        }
        public DbSet<Cliente> Clientes { get; set; }
      
        public DbSet<Categoria> Categorias { get; set; }

        public DbSet<Factura> Facturas { get; set; }

        public DbSet<Producto> Productos { get; set; }

        public DbSet<DetalleVenta> DetalleVentas { get; set; }

        public DbSet<Usuario> usuarios { get; set; }
        public DbSet<LoginRequest> loginRequest { get; set; }


        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<Usuario>().Property(u => u.Rol).HasConversion<string>(); //conversión del enum(rol) a string

            modelBuilder.Entity<LoginRequest>().HasNoKey().ToView(null); // Indica que esta entidad no tiene clave primaria

            base.OnModelCreating(modelBuilder);

        }
    }

}
