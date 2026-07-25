package com.example.ui.theme

import androidx.compose.ui.graphics.Color
import com.example.data.model.AppTheme

data class ThemePalette(
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val secondary: Color,
    val tertiary: Color,
    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val onSurface: Color
)

fun getThemePalette(theme: AppTheme, isDark: Boolean): ThemePalette {
    return when (theme) {
        AppTheme.MATCHA -> if (isDark) ThemePalette(
            primary = Color(0xFFB4C79F),
            onPrimary = Color(0xFF101908),
            primaryContainer = Color(0xFF2E3D22),
            onPrimaryContainer = Color(0xFFD6E8C0),
            secondary = Color(0xFFC2CDB6),
            tertiary = Color(0xFFA1CFC3),
            background = Color(0xFF0F0F0F),
            surface = Color(0xFF181818),
            surfaceVariant = Color(0xFF252525),
            onSurface = Color(0xFFF0F2ED)
        ) else ThemePalette(
            primary = Color(0xFF53683F),
            onPrimary = Color(0xFFFFFFFF),
            primaryContainer = Color(0xFFD5E8BF),
            onPrimaryContainer = Color(0xFF131F06),
            secondary = Color(0xFF5A6351),
            tertiary = Color(0xFF3B665C),
            background = Color(0xFFF9FAEF),
            surface = Color(0xFFF1F3E7),
            surfaceVariant = Color(0xFFE0E5D4),
            onSurface = Color(0xFF1A1C16)
        )

        AppTheme.MINT -> if (isDark) ThemePalette(
            primary = Color(0xFF82DAA8),
            onPrimary = Color(0xFF003820),
            primaryContainer = Color(0xFF005231),
            onPrimaryContainer = Color(0xFF9DF7C3),
            secondary = Color(0xFFB5CCBB),
            tertiary = Color(0xFFA2CED9),
            background = Color(0xFF0F1511),
            surface = Color(0xFF17201A),
            surfaceVariant = Color(0xFF28342B),
            onSurface = Color(0xFFE1E3DF)
        ) else ThemePalette(
            primary = Color(0xFF116C46),
            onPrimary = Color(0xFFFFFFFF),
            primaryContainer = Color(0xFFA3F5C3),
            onPrimaryContainer = Color(0xFF002111),
            secondary = Color(0xFF4E6354),
            tertiary = Color(0xFF3B6571),
            background = Color(0xFFF6FBF7),
            surface = Color(0xFFEEF5EF),
            surfaceVariant = Color(0xFFDAE5DC),
            onSurface = Color(0xFF171D19)
        )

        AppTheme.PEACH -> if (isDark) ThemePalette(
            primary = Color(0xFFFFB5A0),
            onPrimary = Color(0xFF561F0F),
            primaryContainer = Color(0xFF723523),
            onPrimaryContainer = Color(0xFFFFDBD1),
            secondary = Color(0xFFE7BDB2),
            tertiary = Color(0xFFE5C38C),
            background = Color(0xFF201A18),
            surface = Color(0xFF29221F),
            surfaceVariant = Color(0xFF3B2F2A),
            onSurface = Color(0xFFEDE0DC)
        ) else ThemePalette(
            primary = Color(0xFF8F4C38),
            onPrimary = Color(0xFFFFFFFF),
            primaryContainer = Color(0xFFFFDBD1),
            onPrimaryContainer = Color(0xFF3A0B01),
            secondary = Color(0xFF77574E),
            tertiary = Color(0xFF6C5A2F),
            background = Color(0xFFFFF8F6),
            surface = Color(0xFFFAF0EC),
            surfaceVariant = Color(0xFFF5DDD6),
            onSurface = Color(0xFF201A18)
        )

        AppTheme.CORAL -> if (isDark) ThemePalette(
            primary = Color(0xFFFFB4AA),
            onPrimary = Color(0xFF561D18),
            primaryContainer = Color(0xFF73332C),
            onPrimaryContainer = Color(0xFFFFDAD5),
            secondary = Color(0xFFE7BDB8),
            tertiary = Color(0xFFE0C38C),
            background = Color(0xFF201A19),
            surface = Color(0xFF2B2220),
            surfaceVariant = Color(0xFF3E302E),
            onSurface = Color(0xFFEDE0DE)
        ) else ThemePalette(
            primary = Color(0xFF904A43),
            onPrimary = Color(0xFFFFFFFF),
            primaryContainer = Color(0xFFFFDAD5),
            onPrimaryContainer = Color(0xFF3B0907),
            secondary = Color(0xFF775652),
            tertiary = Color(0xFF6A5B2E),
            background = Color(0xFFFFF8F7),
            surface = Color(0xFFFAF0EE),
            surfaceVariant = Color(0xFFF5DDD9),
            onSurface = Color(0xFF201A19)
        )

        AppTheme.LAVENDER -> if (isDark) ThemePalette(
            primary = Color(0xFFCFBCFF),
            onPrimary = Color(0xFF381E72),
            primaryContainer = Color(0xFF4F358A),
            onPrimaryContainer = Color(0xFFEADDFF),
            secondary = Color(0xFFCBC2DB),
            tertiary = Color(0xFFEFB8C8),
            background = Color(0xFF1C1B1F),
            surface = Color(0xFF25232A),
            surfaceVariant = Color(0xFF36333D),
            onSurface = Color(0xFFE6E1E5)
        ) else ThemePalette(
            primary = Color(0xFF6750A4),
            onPrimary = Color(0xFFFFFFFF),
            primaryContainer = Color(0xFFEADDFF),
            onPrimaryContainer = Color(0xFF21005D),
            secondary = Color(0xFF625B71),
            tertiary = Color(0xFF7D5260),
            background = Color(0xFFFEF7FF),
            surface = Color(0xFFF7F2FA),
            surfaceVariant = Color(0xFFE7E0EC),
            onSurface = Color(0xFF1D1B20)
        )

        AppTheme.SKY_BLUE -> if (isDark) ThemePalette(
            primary = Color(0xFF91CEF4),
            onPrimary = Color(0xFF00344B),
            primaryContainer = Color(0xFF004C6C),
            onPrimaryContainer = Color(0xFFC2E8FF),
            secondary = Color(0xFFB6C9D8),
            tertiary = Color(0xFFCDC0E8),
            background = Color(0xFF101417),
            surface = Color(0xFF181C20),
            surfaceVariant = Color(0xFF283036),
            onSurface = Color(0xFFE0E2E6)
        ) else ThemePalette(
            primary = Color(0xFF1D6586),
            onPrimary = Color(0xFFFFFFFF),
            primaryContainer = Color(0xFFC2E8FF),
            onPrimaryContainer = Color(0xFF001E2C),
            secondary = Color(0xFF4E616D),
            tertiary = Color(0xFF62597C),
            background = Color(0xFFF7FBFD),
            surface = Color(0xFFEEF5F8),
            surfaceVariant = Color(0xFFDDE4E9),
            onSurface = Color(0xFF191C1E)
        )

        AppTheme.OCEAN -> if (isDark) ThemePalette(
            primary = Color(0xFF7CD0FF),
            onPrimary = Color(0xFF00344A),
            primaryContainer = Color(0xFF004C6A),
            onPrimaryContainer = Color(0xFFC4E7FF),
            secondary = Color(0xFFB5C9D7),
            tertiary = Color(0xFFC8C3EA),
            background = Color(0xFF0F1417),
            surface = Color(0xFF171D21),
            surfaceVariant = Color(0xFF273138),
            onSurface = Color(0xFFDFE3E7)
        ) else ThemePalette(
            primary = Color(0xFF00658C),
            onPrimary = Color(0xFFFFFFFF),
            primaryContainer = Color(0xFFC4E7FF),
            onPrimaryContainer = Color(0xFF001E2D),
            secondary = Color(0xFF4E616E),
            tertiary = Color(0xFF5F5A7D),
            background = Color(0xFFF6FAF9),
            surface = Color(0xFFEDF4F7),
            surfaceVariant = Color(0xFFDAE3EA),
            onSurface = Color(0xFF171C1F)
        )

        AppTheme.SAKURA_PINK -> if (isDark) ThemePalette(
            primary = Color(0xFFFFB0C9),
            onPrimary = Color(0xFF5E1133),
            primaryContainer = Color(0xFF7B294A),
            onPrimaryContainer = Color(0xFFFFD8E4),
            secondary = Color(0xFFE3BDC7),
            tertiary = Color(0xFFE2C0A5),
            background = Color(0xFF201A1C),
            surface = Color(0xFF2B2225),
            surfaceVariant = Color(0xFF3E3034),
            onSurface = Color(0xFFECE0E2)
        ) else ThemePalette(
            primary = Color(0xFF984062),
            onPrimary = Color(0xFFFFFFFF),
            primaryContainer = Color(0xFFFFD8E4),
            onPrimaryContainer = Color(0xFF3E001E),
            secondary = Color(0xFF75565F),
            tertiary = Color(0xFF7A583E),
            background = Color(0xFFFFF8F9),
            surface = Color(0xFFFAF0F3),
            surfaceVariant = Color(0xFFF2DDE2),
            onSurface = Color(0xFF201A1C)
        )

        AppTheme.MIDNIGHT -> ThemePalette(
            primary = Color(0xFFE0E2E5),
            onPrimary = Color(0xFF191C1E),
            primaryContainer = Color(0xFF333538),
            onPrimaryContainer = Color(0xFFF0F1F3),
            secondary = Color(0xFFC4C7CB),
            tertiary = Color(0xFFD4C3DF),
            background = Color(0xFF0A0C0E),
            surface = Color(0xFF121417),
            surfaceVariant = Color(0xFF222528),
            onSurface = Color(0xFFE0E2E5)
        )

        else -> if (isDark) ThemePalette(
            primary = Color(0xFFDFC2A3),
            onPrimary = Color(0xFF3F2D17),
            primaryContainer = Color(0xFF57432B),
            onPrimaryContainer = Color(0xFFFDDEBD),
            secondary = Color(0xFFD4C4B5),
            tertiary = Color(0xFFC0CAAC),
            background = Color(0xFF1A1714),
            surface = Color(0xFF24201C),
            surfaceVariant = Color(0xFF352F2A),
            onSurface = Color(0xFFEBE0D8)
        ) else ThemePalette(
            primary = Color(0xFF715B41),
            onPrimary = Color(0xFFFFFFFF),
            primaryContainer = Color(0xFFFDDEBD),
            onPrimaryContainer = Color(0xFF281805),
            secondary = Color(0xFF6B5D50),
            tertiary = Color(0xFF586349),
            background = Color(0xFFFFF8F4),
            surface = Color(0xFFF9EFE7),
            surfaceVariant = Color(0xFFEFE0D3),
            onSurface = Color(0xFF1F1B16)
        )
    }
}
