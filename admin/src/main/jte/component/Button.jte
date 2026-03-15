@param String type = "button"
@param String variant = "primary"
@param String size = "md"
@param String text = ""
@param String href = ""
@param boolean disabled = false

!{
    var variantClass = switch (variant) {
        case "secondary" -> "bg-white border border-border text-text-primary hover:bg-slate-50 dark:bg-slate-700 dark:border-slate-600 dark:text-slate-200 dark:hover:bg-slate-600";
        case "danger"    -> "bg-danger hover:bg-red-700 text-white";
        case "ghost"     -> "bg-transparent text-primary hover:bg-primary/10";
        default          -> "bg-primary hover:bg-secondary text-white";
    };
    var sizeClass = switch (size) {
        case "sm" -> "px-3 py-1.5 text-xs";
        case "lg" -> "px-6 py-3 text-base";
        default   -> "px-4 py-2.5 text-sm";
    };
    var baseClass = "inline-flex items-center justify-center font-medium rounded-btn transition-colors duration-200 " + variantClass + " " + sizeClass;
}

@if(!href.isEmpty())
    <a href="${href}" class="${baseClass}">${text}</a>
@elseif(disabled)
    <button type="${type}" class="${baseClass}" disabled>
        ${text}
    </button>
@else
    <button type="${type}" class="${baseClass}">
        ${text}
    </button>
@endif
