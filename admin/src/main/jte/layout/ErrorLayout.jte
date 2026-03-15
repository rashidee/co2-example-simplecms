@import gg.jte.Content
@import com.simplecms.adminportal.shared.vite.ViteManifestService

@param Content content
@param String pageTitle = "Error"
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

    <div class="text-center px-6">
        ${content}
    </div>

</body>
</html>
