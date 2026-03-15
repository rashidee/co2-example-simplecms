@param int status = 403
@param String error = "Forbidden"
@param String message = "You do not have permission to access this resource."
@param com.simplecms.adminportal.shared.vite.ViteManifestService vite
@param String cspNonce = ""

@template.layout.ErrorLayout(
    vite = vite,
    cspNonce = cspNonce,
    pageTitle = "403 Forbidden",
    content = @`
    <div class="text-6xl font-bold text-danger mb-4">403</div>
    <h1 class="text-2xl font-semibold text-text-primary mb-2">${error}</h1>
    <p class="text-text-secondary mb-6">${message}</p>
    <a href="/" class="inline-flex items-center px-4 py-2.5 bg-primary hover:bg-secondary text-white text-sm font-medium rounded-btn transition-colors">
        Go Home
    </a>
    `
)
