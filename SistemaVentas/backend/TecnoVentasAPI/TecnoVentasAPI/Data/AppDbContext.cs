using TecnoVentasAPI.Models;
using Microsoft.EntityFrameworkCore;

namespace TecnoVentasAPI.Data
{
    public class AppDbContext : DbContext

    {
        public AppDbContext(DbContextOptions<AppDbContext> options) : base(options)
        {
        }
        public DbSet<Cliente> Clientes { get; set; }
        public DbSet<Factura> Facturas { get; set; }
        public DbSet<Producto> Productos { get; set; }
        public DbSet<DetalleFactura> DetalleFacturas { get; set; }
        /*protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<Cliente>()
                .HasMany(c => c.Facturas)
                .WithOne(f => f.Cliente)
                .HasForeignKey(f => f.IdCliente);
            modelBuilder.Entity<Factura>()
                .HasMany(f => f.DetalleFacturas)
                .WithOne(df => df.Factura)
                .HasForeignKey(df => df.IdFactura);
            modelBuilder.Entity<Producto>()
                .HasMany(p => p.DetalleFacturas)
                .WithOne(df => df.Producto)
                .HasForeignKey(df => df.IdProducto);
        }*/
    }
}
