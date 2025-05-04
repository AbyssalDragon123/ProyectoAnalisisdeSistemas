using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace TecnoVentasAPI.Models
{
    [Table("usuario")]
    public class Usuario
    {
        [Key]
        [Column("id_usuario")]
        public int IdUsuario { get; set; }

        [Required]
        [MaxLength(45)]
        [Column("nombre")]
        public string Nombre { get; set; } = string.Empty;

        [Required]
        [MaxLength(45)]
        [Column("apellido")]
        public string Apellido { get; set; } = string.Empty;

        [Required]
        [MaxLength(45)]
        [Column("correo")]
        public string Correo { get; set; } = string.Empty;

        [Required]
        [MaxLength(45)]
        [Column("username")]
        public string Username { get; set; } = string.Empty;

        [Required]
        [MaxLength(255)]
        [Column("pass")]
        public string Pass { get; set; } = string.Empty;

        [Required]
        [Column("rol")]
        public RolUsuario Rol { get; set; } = RolUsuario.cajero;

        [Column("password_reset_token")]
        [MaxLength(255)]
        public string? PasswordResetToken { get; set; }

        [Column("password_reset_expires")]
        public DateTime? PasswordResetExpires { get; set; }

        // Relaciones de navegación
        public ICollection<Cliente>? Clientes { get; set; }
        public ICollection<Factura>? Facturas { get; set; }
        public ICollection<Producto>? Productos { get; set; }
    }

    public enum RolUsuario
    {
        admin,
        vendedor,
        cajero
    }
}

