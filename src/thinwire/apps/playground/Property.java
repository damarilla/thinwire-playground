/*
 *                        ThinWire(TM) Playground Demo
 *                 Copyright (C) 2006 Custom Credit Systems
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Users wishing to use this demo in proprietary products which are not 
 * themselves to be released under the GNU Public License should contact Custom
 * Credit Systems for a license to do so.
 * 
 *               Custom Credit Systems, Richardson, TX 75081, USA.
 *                          http://www.thinwire.com
 */
package thinwire.apps.playground;

import java.lang.reflect.*;

import thinwire.ui.*;
import thinwire.ui.style.*;

/**
 * @author Joshua J. Gertzen
 */
enum Property {
    X(Component.class, Component.PROPERTY_X, int.class),
    Y(Component.class, Component.PROPERTY_Y, int.class),
    WIDTH(Component.class, Component.PROPERTY_WIDTH, int.class),
    HEIGHT(Component.class, Component.PROPERTY_HEIGHT, int.class),
    ENABLED(Component.class, Component.PROPERTY_ENABLED, boolean.class),
    VISIBLE(Component.class, Component.PROPERTY_VISIBLE, boolean.class),
    FOCUS_CAPABLE(Component.class, Component.PROPERTY_FOCUS_CAPABLE, boolean.class),
    TEXT(TextComponent.class, TextComponent.PROPERTY_TEXT, String.class),
    IMAGE(ImageComponent.class, ImageComponent.PROPERTY_IMAGE, String.class),
    ALIGN_X(AlignTextComponent.class, AlignTextComponent.PROPERTY_ALIGN_X, AlignX.class),
    MAX_LENGTH(EditorComponent.class, EditorComponent.PROPERTY_MAX_LENGTH, int.class),
    EDIT_MASK(MaskEditorComponent.class, MaskEditorComponent.PROPERTY_EDIT_MASK, String.class),
    FORMAT_TEXT(MaskEditorComponent.class, MaskEditorComponent.PROPERTY_FORMAT_TEXT, boolean.class),
    TITLE(Window.class, Window.PROPERTY_TITLE, String.class),
    WAIT_FOR_WINDOW(Window.class, Window.PROPERTY_WAIT_FOR_WINDOW, boolean.class),
    EDIT_ALLOWED(DropDown.class, DropDown.PROPERTY_EDIT_ALLOWED, boolean.class),
    INPUT_HIDDEN(TextField.class, TextField.PROPERTY_INPUT_HIDDEN, boolean.class),
    SCROLL(Container.class, Container.PROPERTY_SCROLL, ScrollType.class),
    STANDARD(Button.class, Button.PROPERTY_STANDARD, boolean.class),
    BUTTON_CHECKED(RadioButton.class, RadioButton.PROPERTY_CHECKED, boolean.class),
    BOX_CHECKED(CheckBox.class, CheckBox.PROPERTY_CHECKED, boolean.class),
    LINK_LOCATION(Hyperlink.class, Hyperlink.PROPERTY_LOCATION, String.class),
    FULL_ROW_CHECK_BOX(GridBox.class, GridBox.PROPERTY_FULL_ROW_CHECK_BOX, boolean.class),
    VISIBLE_CHECK_BOXES(GridBox.class, GridBox.PROPERTY_VISIBLE_CHECK_BOXES, boolean.class),
    VISIBLE_HEADER(GridBox.class, GridBox.PROPERTY_VISIBLE_HEADER, boolean.class),
    BROWSER_LOCATION(WebBrowser.class, WebBrowser.PROPERTY_LOCATION, String.class),
    ROOT_ITEM_VISIBLE(Tree.class, Tree.PROPERTY_ROOT_ITEM_VISIBLE, boolean.class),
    RESIZE_ALLOWED(Dialog.class, Dialog.PROPERTY_RESIZE_ALLOWED, boolean.class),
    BACKGROUND_COLOR(Background.class, Background.PROPERTY_BACKGROUND_COLOR, Color.class),
    FONT_BOLD(Font.class, Font.PROPERTY_FONT_BOLD, boolean.class),
    FONT_ITALIC(Font.class, Font.PROPERTY_FONT_ITALIC, boolean.class),
    FONT_UNDERLINE(Font.class, Font.PROPERTY_FONT_UNDERLINE, boolean.class),
    FONT_FAMILY(Font.class, Font.PROPERTY_FONT_FAMILY, Font.Family.class),
    FONT_SIZE(Font.class, Font.PROPERTY_FONT_SIZE, int.class),
    FONT_COLOR(Font.class, Font.PROPERTY_FONT_COLOR, Color.class),
    BORDER_TYPE(Border.class, Border.PROPERTY_BORDER_TYPE, Border.Type.class),
    BORDER_SIZE(Border.class, Border.PROPERTY_BORDER_SIZE, int.class),
    BORDER_COLOR(Border.class, Border.PROPERTY_BORDER_COLOR, Color.class),
    FX_POSITION_CHANGE(FX.class, FX.PROPERTY_FX_POSITION_CHANGE, FX.Type.class),
    FX_SIZE_CHANGE(FX.class, FX.PROPERTY_FX_SIZE_CHANGE, FX.Type.class),
    FX_VISIBLE_CHANGE(FX.class, FX.PROPERTY_FX_VISIBLE_CHANGE, FX.Type.class),
    LENGTH(RangeComponent.class, RangeComponent.PROPERTY_LENGTH, int.class),
    TF_CURRENT_INDEX(TabFolder.class, TabFolder.PROPERTY_CURRENT_INDEX, int.class),
    CURRENT_INDEX(RangeComponent.class, RangeComponent.PROPERTY_CURRENT_INDEX, int.class),
    WRAP_TEXT(Label.class, Label.PROPERTY_WRAP_TEXT, boolean.class);
    
    private static final String[] TRUE_FALSE = new String[]{"true", "false"};
    
    private Class objectType;
    private String name;
    private Class type;
    private Method getter;
    private Method setter;
    
    private Property(Class objectType, String name, Class type) {
        try {
            this.objectType = objectType;
            this.name = name;
            this.type = type;            
            if (isStyleProperty()) name = name.substring(Main.getSimpleClassName(objectType).length());
            name = name.length() == 1 ? String.valueOf(Character.toUpperCase(name.charAt(0))) : Character.toUpperCase(name.charAt(0)) + name.substring(1);            
            getter = objectType.getMethod((type == boolean.class ? "is" : "get") + name);
            setter = objectType.getMethod("set" + name, type);
        } catch (Exception e) {
            if (e instanceof RuntimeException) throw (RuntimeException)e;
            throw new RuntimeException(e);
        }
    }
    
    public Class getObjectType() {
        return objectType;
    }
    
    public String getName() {
        return name;
    }
    
    public Class getType() {
        return type;
    }
    
    public MaskEditorComponent newEditor() {
        String[] options = null;
        boolean editAllowed = true;
        String editMask = null;
        int maxLength = -1;
        AlignX alignX = null;
        
        if (type == boolean.class) {
            options = TRUE_FALSE;
            editAllowed = false;
        } else if (type == int.class) {
            editMask = name == Component.PROPERTY_X || name == Component.PROPERTY_Y ? "-####" : "####";
            alignX = AlignX.RIGHT;
        } else if (name == MaskEditorComponent.PROPERTY_EDIT_MASK) {
            options = new String[] {
                    "-###,###,###.##",
                    "###.####",
                    "MM/dd/yyyy",
                    "MM/dd/yy",
                    "hh:mm",
                    "999-99-9999",
                    "99-9999999",
                    "(999) 999-9999",
                    "XXXXXXXX",
                    "AAAAAAAA",
                    "aaaaaaaa",
            };
        } else if (name == ImageComponent.PROPERTY_IMAGE) {
            Widget[] values = Widget.values();
            options = new String[values.length + 2];
            options[0] = Main.RES_PATH + "File.png";
            options[1] = Main.RES_PATH + "Folder.png";
            
            for (int i = 0; i < values.length; i++) {
                options[i + 2] = Main.RES_PATH + Main.getSimpleClassName(values[i].getType()) + ".png";
            }
        } else if (name == WebBrowser.PROPERTY_LOCATION) {
            options = new String[] {
                    "http://www.thinwire.com",
                    "http://www.customcreditsystems.com",
                    "http://www.truecode.org",
                    Main.RES_PATH + "About.html"
            };
        } else if (Enum.class.isAssignableFrom(type)) {            
            try {
                editAllowed = false;
                Enum[] values = (Enum[])type.getMethod("values").invoke(null);                
                options = new String[values.length];
                
                for (int i = 0; i < values.length; i++) {
                    options[i] = values[i].name().toLowerCase();
                }                
            } catch (Exception e) {
                if (e instanceof RuntimeException) throw (RuntimeException)e;
                throw new RuntimeException(e);
            }            
        } else if (type == Color.class) {
            Color[] values = Color.values();
            options = new String[values.length + 2];
            editAllowed = true;
            options[0] = "rgb(255, 128, 0)";
            options[1] = "#5F9EA0";

            for (int i = 0; i < values.length; i++) {
                options[i + 2] = values[i].name().toLowerCase();
            }
        } else if (type == Font.Family.class) {
            Font.Family[] values = Font.Family.values();
            options = new String[values.length + 1];
            editAllowed = true;
            options[0] = "Tahoma, sans-serif";

            for (int i = 0; i < values.length; i++) {
                options[i + 1] = values[i].toString();
            }
        }
        
        MaskEditorComponent editor;
        
        if (options != null) {
            DropDownGridBox dd = new DropDownGridBox();
            dd.getComponent().getColumns().add(new GridBox.Column((Object[])options));
            dd.setEditAllowed(editAllowed);
            editor = dd;
        } else {
            editor = new TextField();
        }
        
        if (editMask != null) {
            editor.setEditMask(editMask);
        } else if (maxLength != -1) {
            editor.setMaxLength(maxLength);
        }
        
        if (alignX != null) editor.setAlignX(alignX);        
        return editor;
    }
    
    public boolean isValidFor(Component comp) {        
        if (objectType.isInstance(comp) || isStyleProperty()) {
            if (this == SCROLL && comp instanceof TabFolder) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
    public boolean isStyleProperty() {
        return !Component.class.isAssignableFrom(objectType);
    }
    
    public void setValue(Component comp, Object value) {
        try {
            if (!type.isInstance(value)) value = convertTo(type, value.toString());
            
            if (objectType == Background.class) {
                setter.invoke(comp.getStyle().getBackground(), value);
            } else if (objectType == Font.class) {
                setter.invoke(comp.getStyle().getFont(), value);
            } else if (objectType == Border.class) {
                setter.invoke(comp.getStyle().getBorder(), value);
            } else if (objectType == FX.class) {
                setter.invoke(comp.getStyle().getFX(), value);
            } else {
                String name = getName();
                
                if (name.equals(Component.PROPERTY_VISIBLE) || name.equals(Component.PROPERTY_X) || name.equals(Component.PROPERTY_Y) || 
                        name.equals(Component.PROPERTY_WIDTH) || name.equals(Component.PROPERTY_HEIGHT)) {
                    FX fx = comp.getStyle().getFX();
                    FX.Type ftpc = fx.getPositionChange();
                    FX.Type ftsc = fx.getSizeChange();
                    FX.Type ftvc = fx.getVisibleChange();
                    fx.setPositionChange(FX.Type.SMOOTH);
                    fx.setSizeChange(FX.Type.SMOOTH);
                    fx.setVisibleChange(FX.Type.SMOOTH);
                    setter.invoke(comp, value);
                    fx.setPositionChange(ftpc);
                    fx.setSizeChange(ftsc);
                    fx.setVisibleChange(ftvc);
                } else {
                    setter.invoke(comp, value);                    
                }
            }
        } catch (Exception e) {
            if (e instanceof RuntimeException) throw (RuntimeException)e;
            throw new RuntimeException(e);
        }
    }

    public Object getValue(Component comp) {
        try {
            if (objectType == Background.class) {
                return getter.invoke(comp.getStyle().getBackground());
            } else if (objectType == Font.class) {
                return getter.invoke(comp.getStyle().getFont());
            } else if (objectType == Border.class) {
                return getter.invoke(comp.getStyle().getBorder());
            } else if (objectType == FX.class) {
                return getter.invoke(comp.getStyle().getFX());
            } else {            
                return getter.invoke(comp);
            }
        } catch (Exception e) {
            if (e instanceof RuntimeException) throw (RuntimeException)e;
            throw new RuntimeException(e);
        }
    }
    
    public Object getDefaultValue(Component comp) {
        for (Widget w : Widget.values()) {           
            if (w.getType().isInstance(comp)) {
                return w.getDefaultValue(name);
            }
        }
        
        return null;
    }
    
    private static Object convertTo(Class type, Object value) {
        if (value == null) return null;
        if (type.equals(value.getClass())) return value;        
        String str = value.toString();
        if (str.equals("null")) str = null;
        
        if (type == String.class) {
            value = str;
        } else if (type == boolean.class || type == Boolean.class) {
            value = Boolean.valueOf(str);
        } else if (type == int.class || type == Integer.class) {                            
            value = new Integer(Double.valueOf(str).intValue());
        } else if (type == long.class || type == Long.class) {                            
            value = new Long(Double.valueOf(str).longValue());
        } else if (type == short.class || type == Short.class) {
            value = new Short(Double.valueOf(str).shortValue());
        } else if (type == byte.class || type == Byte.class) {
            value = new Byte(Double.valueOf(str).byteValue());
        } else if (type == float.class || type == Float.class) {
            value = new Float(Double.valueOf(str).floatValue());
        } else if (type == double.class || type == Double.class) {
            value = Double.valueOf(str);                                
        } else if (type == char.class || type == Character.class) {                                
            value = new Character(str.charAt(0));
        } else {
            try {
                Field f = type.getField(str.toUpperCase().replace('-', '_'));                        
                value = f.get(null);
            } catch (NoSuchFieldException e2) {
                try {
                    Method m = type.getMethod("valueOf", String.class);
                    if (m.getReturnType() != type) throw new NoSuchMethodException("public static " + type + " valueOf(String value)");
                    value = m.invoke(null, value);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);                
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            } catch (IllegalAccessException e2) {
                throw new RuntimeException(e2);
            }
        }
        
        return value;
    }    
}