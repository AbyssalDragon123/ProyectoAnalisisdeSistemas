using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Logging;
using System;
using System.Linq;
using System.Threading.Tasks;

namespace TecnoVentasAPI.Util
{
    public class TokenLoggingMiddleware
    {
        private readonly RequestDelegate _next;

        private readonly ILogger<TokenLoggingMiddleware> _logger;

        public TokenLoggingMiddleware(RequestDelegate next, ILogger<TokenLoggingMiddleware> logger)
        {
            _next = next;
            _logger = logger;
        }

        public async Task Invoke(HttpContext context)
        {
            _logger.LogInformation("[TokenLoggingMiddleware] Middleware ejecutándose");

            var token = context.Request.Headers["Authorization"].FirstOrDefault();

            if (!string.IsNullOrEmpty(token))
            {
                _logger.LogInformation($"Token recibido: {token} - Ruta: {context.Request.Path} - Hora: {DateTime.Now}");
            }
            else
            {
                _logger.LogInformation($"No se recibió token - Ruta: {context.Request.Path}");
            }

            await _next(context);
        }
    }
}
