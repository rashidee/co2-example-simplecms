@import gg.jte.Content
@import com.simplecms.adminportal.shared.vite.ViteManifestService

@param Content content
@param String pageTitle = "Admin Portal"
@param String currentPath = ""
@param String userRole = ""
@param String userInitials = ""
@param String userName = ""
@param ViteManifestService vite
@param String cspNonce = ""

<!DOCTYPE html>
<html lang="en" x-data :class="{ 'dark': $store.theme.dark }">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle} - Simple CMS</title>
    <link rel="stylesheet" href="${vite.resolve("src/main.css")}">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body class="bg-page-bg dark:bg-slate-900 text-text-primary dark:text-slate-200 min-h-screen flex flex-col font-body">

    @template.fragment.Header(
        userInitials = userInitials,
        userName = userName,
        userRole = userRole,
        cspNonce = cspNonce
    )

    <div class="flex pt-16 min-h-[calc(100vh-4rem)]">
        @template.fragment.Sidebar(
            currentPath = currentPath,
            userRole = userRole
        )

        <main class="flex-1 ml-64 flex flex-col min-w-0">
            <div id="content-spinner" class="hidden fixed inset-0 bg-white/50 dark:bg-slate-900/50 z-50 items-center justify-center">
                <div class="w-8 h-8 border-4 border-primary border-t-transparent rounded-full animate-spin"></div>
            </div>
            <div id="content-area" class="flex-1 flex flex-col transition-opacity duration-200 p-6">
                ${content}
            </div>
            @template.fragment.Footer()
        </main>
    </div>

    @template.component.Toast()
    <script src="${vite.resolve("src/main.js")}" nonce="${cspNonce}"></script>
</body>
</html>
