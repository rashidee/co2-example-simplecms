@import gg.jte.Content
@import com.simplecms.adminportal.shared.vite.ViteManifestService

@param Content content
@param String pageTitle = "Admin Portal"
@param ViteManifestService vite
@param String cspNonce = ""

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle} - Simple CMS</title>
    <link rel="stylesheet" href="${vite.resolve("src/main.css")}">
</head>
<body class="bg-page-bg min-h-screen flex items-center justify-center font-body">

    <div class="w-full max-w-md px-6">
        <div class="text-center mb-8">
            <h1 class="text-2xl font-bold text-text-primary">Simple CMS</h1>
            <p class="text-text-secondary text-sm mt-1">Admin Portal</p>
        </div>

        ${content}
    </div>

    <script src="${vite.resolve("src/main.js")}" nonce="${cspNonce}"></script>
</body>
</html>
