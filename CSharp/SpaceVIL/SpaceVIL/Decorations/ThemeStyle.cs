using System;
using System.Collections.Generic;
using System.Collections.Concurrent;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using SpaceVIL.Decorations;
using SpaceVIL.Core;
using SpaceVIL.Common;

namespace SpaceVIL.Decorations
{
    public class ThemeStyle
    {
        private Dictionary<Type, Style> DefaultItemsStyle = new Dictionary<Type, Style>();
        // {
        //     {typeof(SpaceVIL.ButtonCore), Style.GetButtonCoreStyle()},
        //     {typeof(SpaceVIL.ButtonToggle), Style.GetButtonToggleStyle()},
        //     {typeof(SpaceVIL.CheckBox), Style.GetCheckBoxStyle()},
        //     {typeof(SpaceVIL.ComboBox), Style.GetComboBoxStyle()},
        //     {typeof(SpaceVIL.ComboBoxDropDown), Style.GetComboBoxDropDownStyle()},
        //     {typeof(SpaceVIL.ContextMenu), Style.GetContextMenuStyle()},
        //     {typeof(SpaceVIL.FlowArea), Style.GetFlowAreaStyle()},
        //     {typeof(SpaceVIL.Frame), Style.GetFrameStyle()},
        //     {typeof(SpaceVIL.Grid), Style.GetGridStyle()},
        //     {typeof(SpaceVIL.HorizontalScrollBar), Style.GetHorizontalScrollBarStyle()},
        //     {typeof(SpaceVIL.HorizontalSlider), Style.GetHorizontalSliderStyle()},
        //     {typeof(SpaceVIL.HorizontalSplitArea), Style.GetHorizontalSplitAreaStyle()},
        //     {typeof(SpaceVIL.HorizontalStack), Style.GetHorizontalStackStyle()},
        //     {typeof(SpaceVIL.Label), Style.GetLabelStyle()},
        //     {typeof(SpaceVIL.ListArea), Style.GetListAreaStyle()},
        //     {typeof(SpaceVIL.ListBox), Style.GetListBoxStyle()},
        //     {typeof(SpaceVIL.MenuItem), Style.GetMenuItemStyle()},
        //     {typeof(SpaceVIL.Indicator), Style.GetIndicatorStyle()},
        //     {typeof(SpaceVIL.RadioButton), Style.GetRadioButtonStyle()},
        //     {typeof(SpaceVIL.PasswordLine), Style.GetPasswordLineStyle()},
        //     {typeof(SpaceVIL.TextEdit), Style.GetTextEditStyle()},
        //     {typeof(SpaceVIL.TextBlock), Style.GetTextBlockStyle()},
        //     {typeof(SpaceVIL.PopUpMessage), Style.GetPopUpMessageStyle()},
        //     {typeof(SpaceVIL.ProgressBar), Style.GetProgressBarStyle()},
        //     {typeof(SpaceVIL.ToolTip), Style.GetToolTipStyle()},
        //     {typeof(SpaceVIL.TitleBar), Style.GetTitleBarStyle()},
        //     {typeof(SpaceVIL.VerticalScrollBar), Style.GetVerticalScrollBarStyle()},
        //     {typeof(SpaceVIL.VerticalSlider), Style.GetVerticalSliderStyle()},
        //     {typeof(SpaceVIL.VerticalSplitArea), Style.GetVerticalSplitAreaStyle()},
        //     {typeof(SpaceVIL.VerticalStack), Style.GetVerticalStackStyle()},
        //     {typeof(SpaceVIL.TabView), Style.GetTabViewStyle()},
        //     // {typeof(SpaceVIL.TextItem), Style.GetTextItemStyle()},

        //     // {typeof(SpaceVIL.CustomSelector), Style.GetCustomSelectorStyle()},
        //     // {typeof(SpaceVIL.WContainer), Style.GetWContainerStyle()},
        // };

        public static bool ApplyEmbedded = true;
        // private static ThemeStyle _instance;

        /// <summary>
        /// Constructs a default ThemeStyle
        /// </summary>
        public ThemeStyle()
        {
            if (ThemeStyle.ApplyEmbedded)
            {
                DefaultItemsStyle.Add(typeof(SpaceVIL.ButtonCore), Style.GetButtonCoreStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.ButtonToggle), Style.GetButtonToggleStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.CheckBox), Style.GetCheckBoxStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.ComboBox), Style.GetComboBoxStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.ComboBoxDropDown), Style.GetComboBoxDropDownStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.ContextMenu), Style.GetContextMenuStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.FreeArea), Style.GetFreeAreaStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.Frame), Style.GetFrameStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.Grid), Style.GetGridStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.HorizontalScrollBar), Style.GetHorizontalScrollBarStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.HorizontalSlider), Style.GetHorizontalSliderStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.HorizontalSplitArea), Style.GetHorizontalSplitAreaStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.HorizontalStack), Style.GetHorizontalStackStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.Label), Style.GetLabelStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.ListArea), Style.GetListAreaStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.ListBox), Style.GetListBoxStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.MenuItem), Style.GetMenuItemStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.Indicator), Style.GetIndicatorStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.RadioButton), Style.GetRadioButtonStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.PasswordLine), Style.GetPasswordLineStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.TextEdit), Style.GetTextEditStyle());
                // DefaultItemsStyle.Add(typeof(SpaceVIL.TextEncrypt), Style.GetTextEncryptStyle());
                // DefaultItemsStyle.Add(typeof(SpaceVIL.TextBlock), Style.GetTextBlockStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.TextArea), Style.GetTextAreaStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.PopUpMessage), Style.GetPopUpMessageStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.ProgressBar), Style.GetProgressBarStyle());
                // DefaultItemsStyle.Add(typeof(SpaceVIL.ToolTip), Style.GetToolTipStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.TitleBar), Style.GetTitleBarStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.VerticalScrollBar), Style.GetVerticalScrollBarStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.VerticalSlider), Style.GetVerticalSliderStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.VerticalSplitArea), Style.GetVerticalSplitAreaStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.VerticalStack), Style.GetVerticalStackStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.TabView), Style.GetTabViewStyle());

                DefaultItemsStyle.Add(typeof(SpaceVIL.TreeView), Style.GetTreeViewStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.TreeItem), Style.GetTreeItemStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.SpinItem), Style.GetSpinItemStyle());

                //new
                DefaultItemsStyle.Add(typeof(SpaceVIL.DialogItem), Style.GetDialogItemStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.MessageItem), Style.GetMessageItemStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.InputDialog), Style.GetInputDialogStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.WContainer), Style.GetWindowContainerStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.OpenEntryDialog), Style.GetOpenEntryDialogStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.FileSystemEntry), Style.GetFileSystemEntryStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.SelectionItem), Style.GetSelectedItemStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.WrapArea), Style.GetWrapAreaStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.WrapGrid), Style.GetWrapGridStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.SideArea), Style.GetSideAreaStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.ImageItem), Style.GetImageItemStyle());
            }
        }

        // public static ThemeStyle GetInstance()
        // {
        //     if (_instance == null)
        //         _instance = new ThemeStyle();
        //     return _instance;
        // }

        /// <summary>
        /// Returns style of the theme for the object by its class name
        /// </summary>
        public Style GetThemeStyle(Type type)
        {
            if (DefaultItemsStyle.ContainsKey(type))
                return DefaultItemsStyle[type];
            return null;
        }

        private ConcurrentDictionary<IBaseItem, Style> SpecificItemsStyle = new ConcurrentDictionary<IBaseItem, Style>();

        /// <summary>
        /// Set this theme as default
        /// </summary>
        public void SetCurrentAsDefault()
        {
            DefaultsService.SetDefaultTheme(this);
        }

        /// <summary>
        /// Add unique style for the item
        /// </summary>
        public void AddSpecificItemStyle(IBaseItem current_item, Style style)
        {
            if (SpecificItemsStyle.ContainsKey(current_item))
                SpecificItemsStyle[current_item] = style;
            else
                SpecificItemsStyle.TryAdd(current_item, style);
        }

        /// <summary>
        /// Remove unique style for the item
        /// </summary>
        public void RemoveSpecificItemStyle(IBaseItem current_item, Style style)
        {
            if (SpecificItemsStyle.ContainsKey(current_item))
                SpecificItemsStyle.TryRemove(current_item, out style);
        }

        /// <summary>
        /// Replace default style for the items with class name class_type
        /// </summary>
        public bool ReplaceDefaultItemStyle(Type class_type, Style style)
        {
            if (DefaultItemsStyle.ContainsKey(class_type))
            {
                DefaultItemsStyle[class_type] = style;
                return true;
            }
            return false;
        }

        /// <summary>
        /// Add custom style to default theme for the items with class name class_type
        /// </summary>
        public void AddDefaultCustomItemStyle(Type class_type, Style style)
        {
            if (DefaultItemsStyle.ContainsKey(class_type))
                DefaultItemsStyle[class_type] = style;
            else
                DefaultItemsStyle.Add(class_type, style);
        }
    }
}