@param int status = 500
@param String error = "Internal Server Error"
@param String message = "An unexpected error occurred. Please try again later."
@param com.simplecms.adminportal.shared.vite.ViteManifestService vite
@param String cspNonce = ""

@template.layout.ErrorLayout(
    vite = vite,
    cspNonce = cspNonce,
    pageTitle = "500 Server Error",
    content = @`
    <div class="text-6xl font-bold text-danger mb-4">500</div>
    <h1 class="text-2xl font-semibold text-text-primary mb-2">${error}</h1>
    <p class="text-text-secondary mb-6">${message}</p>
    <a href="/" class="inline-flex items-center px-4 py-2.5 bg-primary hover:bg-secondary text-white text-sm font-medium rounded-btn transition-colors">
        Go Home
    </a>
    `
)
