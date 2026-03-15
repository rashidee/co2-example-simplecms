@param int status = 404
@param String error = "Not Found"
@param String message = "The page you are looking for does not exist."
@param com.simplecms.adminportal.shared.vite.ViteManifestService vite
@param String cspNonce = ""

@template.layout.ErrorLayout(
    vite = vite,
    cspNonce = cspNonce,
    pageTitle = "404 Not Found",
    content = @`
    <div class="text-6xl font-bold text-warning mb-4">404</div>
    <h1 class="text-2xl font-semibold text-text-primary mb-2">${error}</h1>
    <p class="text-text-secondary mb-6">${message}</p>
    <a href="/" class="inline-flex items-center px-4 py-2.5 bg-primary hover:bg-secondary text-white text-sm font-medium rounded-btn transition-colors">
        Go Home
    </a>
    `
)
