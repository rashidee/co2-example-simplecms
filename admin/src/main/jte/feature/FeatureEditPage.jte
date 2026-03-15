@param com.simplecms.adminportal.feature.internal.FeatureFormView view
@param com.simplecms.adminportal.shared.vite.ViteManifestService vite
@param String currentPath = ""
@param String userRole = ""
@param String userInitials = ""
@param String userName = ""
@param String cspNonce = ""

@template.layout.MainLayout(
    vite = vite,
    currentPath = currentPath,
    userRole = userRole,
    userInitials = userInitials,
    userName = userName,
    cspNonce = cspNonce,
    content = @`
    <div class="max-w-2xl mx-auto">
        <h1 class="text-xl font-bold text-[#1e1e1e] mb-6">Edit Feature</h1>

        <div class="bg-white border border-[#c3c4c7] rounded-[8px] p-6">
            @if(view.hasError())
                <div class="mb-4 p-3 bg-[#d63638]/10 border border-[#d63638]/30 rounded-[4px] text-sm text-[#d63638]">
                    ${view.errorMessage()}
                </div>
            @endif

            <form method="post" action="/features-section/${view.feature().id().toString()}/edit" class="space-y-4">
                <div>
                    <label for="icon" class="block text-sm font-medium text-[#1e1e1e] mb-1">Icon</label>
                    <select id="icon" name="icon" required
                            class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                        <option value="">Select an icon...</option>
                        @if(view.feature().icon().equals("fa-solid fa-bolt"))
                            <option value="fa-solid fa-bolt" selected>&#xf0e7; Bolt</option>
                        @else
                            <option value="fa-solid fa-bolt">&#xf0e7; Bolt</option>
                        @endif
                        @if(view.feature().icon().equals("fa-solid fa-shield-halved"))
                            <option value="fa-solid fa-shield-halved" selected>&#xf3ed; Shield</option>
                        @else
                            <option value="fa-solid fa-shield-halved">&#xf3ed; Shield</option>
                        @endif
                        @if(view.feature().icon().equals("fa-solid fa-rocket"))
                            <option value="fa-solid fa-rocket" selected>&#xf135; Rocket</option>
                        @else
                            <option value="fa-solid fa-rocket">&#xf135; Rocket</option>
                        @endif
                        @if(view.feature().icon().equals("fa-solid fa-gear"))
                            <option value="fa-solid fa-gear" selected>&#xf013; Gear</option>
                        @else
                            <option value="fa-solid fa-gear">&#xf013; Gear</option>
                        @endif
                        @if(view.feature().icon().equals("fa-solid fa-chart-line"))
                            <option value="fa-solid fa-chart-line" selected>&#xf201; Chart Line</option>
                        @else
                            <option value="fa-solid fa-chart-line">&#xf201; Chart Line</option>
                        @endif
                        @if(view.feature().icon().equals("fa-solid fa-cloud"))
                            <option value="fa-solid fa-cloud" selected>&#xf0c2; Cloud</option>
                        @else
                            <option value="fa-solid fa-cloud">&#xf0c2; Cloud</option>
                        @endif
                        @if(view.feature().icon().equals("fa-solid fa-lock"))
                            <option value="fa-solid fa-lock" selected>&#xf023; Lock</option>
                        @else
                            <option value="fa-solid fa-lock">&#xf023; Lock</option>
                        @endif
                        @if(view.feature().icon().equals("fa-solid fa-code"))
                            <option value="fa-solid fa-code" selected>&#xf121; Code</option>
                        @else
                            <option value="fa-solid fa-code">&#xf121; Code</option>
                        @endif
                        @if(view.feature().icon().equals("fa-solid fa-database"))
                            <option value="fa-solid fa-database" selected>&#xf1c0; Database</option>
                        @else
                            <option value="fa-solid fa-database">&#xf1c0; Database</option>
                        @endif
                        @if(view.feature().icon().equals("fa-solid fa-globe"))
                            <option value="fa-solid fa-globe" selected>&#xf0ac; Globe</option>
                        @else
                            <option value="fa-solid fa-globe">&#xf0ac; Globe</option>
                        @endif
                        @if(view.feature().icon().equals("fa-solid fa-heart"))
                            <option value="fa-solid fa-heart" selected>&#xf004; Heart</option>
                        @else
                            <option value="fa-solid fa-heart">&#xf004; Heart</option>
                        @endif
                        @if(view.feature().icon().equals("fa-solid fa-star"))
                            <option value="fa-solid fa-star" selected>&#xf005; Star</option>
                        @else
                            <option value="fa-solid fa-star">&#xf005; Star</option>
                        @endif
                        @if(view.feature().icon().equals("fa-solid fa-users"))
                            <option value="fa-solid fa-users" selected>&#xf0c0; Users</option>
                        @else
                            <option value="fa-solid fa-users">&#xf0c0; Users</option>
                        @endif
                        @if(view.feature().icon().equals("fa-solid fa-bell"))
                            <option value="fa-solid fa-bell" selected>&#xf0f3; Bell</option>
                        @else
                            <option value="fa-solid fa-bell">&#xf0f3; Bell</option>
                        @endif
                        @if(view.feature().icon().equals("fa-solid fa-check-circle"))
                            <option value="fa-solid fa-check-circle" selected>&#xf058; Check Circle</option>
                        @else
                            <option value="fa-solid fa-check-circle">&#xf058; Check Circle</option>
                        @endif
                        @if(view.feature().icon().equals("fa-solid fa-cog"))
                            <option value="fa-solid fa-cog" selected>&#xf013; Cog</option>
                        @else
                            <option value="fa-solid fa-cog">&#xf013; Cog</option>
                        @endif
                        @if(view.feature().icon().equals("fa-solid fa-lightbulb"))
                            <option value="fa-solid fa-lightbulb" selected>&#xf0eb; Lightbulb</option>
                        @else
                            <option value="fa-solid fa-lightbulb">&#xf0eb; Lightbulb</option>
                        @endif
                        @if(view.feature().icon().equals("fa-solid fa-puzzle-piece"))
                            <option value="fa-solid fa-puzzle-piece" selected>&#xf12e; Puzzle Piece</option>
                        @else
                            <option value="fa-solid fa-puzzle-piece">&#xf12e; Puzzle Piece</option>
                        @endif
                        @if(view.feature().icon().equals("fa-solid fa-wrench"))
                            <option value="fa-solid fa-wrench" selected>&#xf0ad; Wrench</option>
                        @else
                            <option value="fa-solid fa-wrench">&#xf0ad; Wrench</option>
                        @endif
                        @if(view.feature().icon().equals("fa-solid fa-mobile-screen"))
                            <option value="fa-solid fa-mobile-screen" selected>&#xf3cf; Mobile</option>
                        @else
                            <option value="fa-solid fa-mobile-screen">&#xf3cf; Mobile</option>
                        @endif
                    </select>
                </div>
                <div>
                    <label for="title" class="block text-sm font-medium text-[#1e1e1e] mb-1">Title</label>
                    <input type="text" id="title" name="title" required maxlength="100"
                           value="${view.feature().title()}"
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                </div>
                <div>
                    <label for="description" class="block text-sm font-medium text-[#1e1e1e] mb-1">Description</label>
                    <textarea id="description" name="description" required maxlength="500" rows="4"
                              class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">${view.feature().description()}</textarea>
                </div>
                <div>
                    <label for="displayOrder" class="block text-sm font-medium text-[#1e1e1e] mb-1">Display Order</label>
                    <input type="number" id="displayOrder" name="displayOrder" required min="0"
                           value="${view.feature().displayOrder()}"
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                </div>
                <div>
                    <label for="status" class="block text-sm font-medium text-[#1e1e1e] mb-1">Status</label>
                    <select id="status" name="status" required
                            class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                        @for(var status : view.statuses())
                            @if(status == view.feature().status())
                                <option value="${status.name()}" selected>${status.name()}</option>
                            @else
                                <option value="${status.name()}">${status.name()}</option>
                            @endif
                        @endfor
                    </select>
                </div>
                <div class="flex gap-3">
                    <button type="submit"
                            class="px-4 py-2.5 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px] transition-colors duration-200">
                        Update Feature
                    </button>
                    <a href="/features-section"
                       class="px-4 py-2.5 bg-white hover:bg-gray-50 text-[#1e1e1e] text-sm font-medium border border-[#c3c4c7] rounded-[4px] transition-colors duration-200">
                        Cancel
                    </a>
                </div>
            </form>
        </div>
    </div>
    `
)
