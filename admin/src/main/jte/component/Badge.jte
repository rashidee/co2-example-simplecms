@param String type = "default"
@param String text = ""

!{
    var classes = switch (type) {
        case "success" -> "bg-success/10 text-success";
        case "warning" -> "bg-warning/10 text-warning";
        case "error"   -> "bg-danger/10 text-danger";
        case "info"    -> "bg-primary/10 text-primary";
        default        -> "bg-slate-100 text-text-secondary dark:bg-slate-700 dark:text-slate-300";
    };
}

<span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${classes}">
    ${text}
</span>
