interface IconProps {
    svg: string
    size?: number
}

// Zeigt ein SVG inline an, damit currentColor die Textfarbe übernimmt.
function Icon({svg, size = 20}: IconProps) {
    return (
        <span
            className="icon"
            style={{width: size, height: size}}
            aria-hidden="true"
            dangerouslySetInnerHTML={{__html: svg}}
        />
    )
}

export default Icon
