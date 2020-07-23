package com.sthoray.allright.metaDataSearch

data class PropertyX(
    val allow_multiple: Int,
    val `class`: Int,
    val created_at: String,
    val default_option_id: Any,
    val display: Int,
    val display_type: Int,
    val form_title: String,
    val form_type: Int,
    val id: Int,
    val is_custom: Int,
    val is_important: Int,
    val options: List<Option>,
    val owner: Int,
    val pivot: PivotX,
    val primitive_type: Int,
    val priority: Int,
    val required: Int,
    val show_in_panel: Int,
    val updated_at: String
)