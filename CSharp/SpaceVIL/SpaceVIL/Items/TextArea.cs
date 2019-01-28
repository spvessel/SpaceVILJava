using System;
using System.Linq;
using System.Drawing;
using System.Collections.Generic;
using SpaceVIL.Core;
using SpaceVIL.Common;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public class TextArea : Prototype
    {
        static int count = 0;
        private Grid _grid = new Grid(2, 2);
        private TextBlock _area = new TextBlock();

        public BlankItem Menu = new BlankItem();
        private bool _is_menu_disabled = false;

        /// <summary>
        /// Set context menu disable true or false
        /// </summary>
        public void DisableMenu(bool value)
        {
            _is_menu_disabled = value;
        }

        private ContextMenu _menu;

        /// <returns> if TextArea editable or not </returns>
        public bool IsEditable()
        {
            return _area.IsEditable;
        }

        /// <summary>
        /// Set TextArea editable true or false
        /// </summary>
        public void SetEditable(bool value)
        {
            _area.IsEditable = value;
        }

        public VerticalScrollBar VScrollBar = new VerticalScrollBar();
        public HorizontalScrollBar HScrollBar = new HorizontalScrollBar();
        private ScrollBarVisibility _v_scrollBarPolicy = ScrollBarVisibility.AsNeeded;// Always;

        /// <summary>
        /// Vertical scroll bar visibility policy (ALWAYS, AS_NEEDED, NEVER) in the TextArea
        /// </summary>
        public ScrollBarVisibility GetVScrollBarVisible()
        {
            return _v_scrollBarPolicy;
        }
        public void SetVScrollBarVisible(ScrollBarVisibility policy)
        {
            _v_scrollBarPolicy = policy;

            if (policy == ScrollBarVisibility.Never)
            {
                VScrollBar.SetDrawable(false);
                Menu.SetVisible(false);
            }
            else if (policy == ScrollBarVisibility.AsNeeded)
            {
                VScrollBar.SetDrawable(false);
                Menu.SetVisible(false);
            }
            else if (policy == ScrollBarVisibility.Always)
            {
                VScrollBar.SetDrawable(true);
                if (!HScrollBar.IsDrawable())
                    Menu.SetVisible(false);
                else
                    Menu.SetVisible(true);
            }

            _grid.UpdateLayout();
            UpdateHorizontalSlider();
            HScrollBar.Slider.UpdateHandler();
        }

        private ScrollBarVisibility _h_scrollBarPolicy = ScrollBarVisibility.AsNeeded; // Always;

        /// <summary>
        /// Horizontal scroll bar visibility policy (ALWAYS, AS_NEEDED, NEVER) in the TextArea
        /// </summary>
        public ScrollBarVisibility GetHScrollBarVisible()
        {
            return _h_scrollBarPolicy;
        }
        public void SetHScrollBarVisible(ScrollBarVisibility policy)
        {
            _h_scrollBarPolicy = policy;

            if (policy == ScrollBarVisibility.Never)
            {
                HScrollBar.SetDrawable(false);
                Menu.SetVisible(false);
            }
            else if (policy == ScrollBarVisibility.AsNeeded)
            {
                HScrollBar.SetDrawable(false);
                Menu.SetVisible(false);
            }
            else if (policy == ScrollBarVisibility.Always)
            {
                HScrollBar.SetDrawable(true);
                if (!VScrollBar.IsDrawable())
                    Menu.SetVisible(false);
                else
                    Menu.SetVisible(true);
            }

            _grid.UpdateLayout();
            UpdateVerticalSlider();
            VScrollBar.Slider.UpdateHandler();
        }

        /// <summary>
        /// Constructs a TextArea
        /// </summary>
        public TextArea()
        {
            SetItemName("TextArea_" + count);
            count++;
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.TextArea)));

            //VBar
            VScrollBar.IsFocusable = false;
            VScrollBar.SetDrawable(true);
            VScrollBar.SetItemName(GetItemName() + "_" + VScrollBar.GetItemName());

            //HBar
            HScrollBar.IsFocusable = false;
            HScrollBar.SetDrawable(true);
            HScrollBar.SetItemName(GetItemName() + "_" + HScrollBar.GetItemName());

            //Area
            _area.SetItemName(GetItemName() + "_" + _area.GetItemName());
        }

        private Int64 v_size = 0;
        private Int64 h_size = 0;

        private void UpdateVListArea()
        {
            //vertical slider
            float v_value = VScrollBar.Slider.GetCurrentValue();
            int v_offSet = (int)((float)(v_size * v_value) / 100.0f);
            _area.SetScrollYOffset(-v_offSet);
        }

        private void UpdateHListArea()
        {
            //horizontal slider
            float h_value = HScrollBar.Slider.GetCurrentValue();
            int h_offSet = (int)((float)(h_size * h_value) / 100.0f);
            _area.SetScrollXOffset(-h_offSet);
        }

        private void UpdateVerticalSlider()//vertical slider
        {
            int visible_area = _area.GetHeight() - _area.GetPadding().Top - _area.GetPadding().Bottom;
            int total = _area.GetTextHeight();

            int total_invisible_size = total - visible_area;
            if (total <= visible_area)
            {
                VScrollBar.Slider.Handler.SetHeight(0);
                VScrollBar.Slider.SetStep(VScrollBar.Slider.GetMaxValue());
                v_size = 0;
                VScrollBar.Slider.SetCurrentValue(0);
                if (GetVScrollBarVisible() == ScrollBarVisibility.AsNeeded)
                {
                    VScrollBar.SetDrawable(false);
                    Menu.SetVisible(false);
                    _grid.UpdateLayout();
                }
                return;
            }
            if (GetVScrollBarVisible() == ScrollBarVisibility.AsNeeded)
            {
                VScrollBar.SetDrawable(true);
                if (!HScrollBar.IsDrawable())
                    Menu.SetVisible(false);
                else
                    Menu.SetVisible(true);
                _grid.UpdateLayout();
            }
            v_size = total_invisible_size;

            if (total_invisible_size > 0)
            {
                float size_handler = (float)(visible_area)
                    / (float)total * 100.0f;
                size_handler = (float)VScrollBar.Slider.GetHeight() / 100.0f * size_handler;
                //size of handler
                VScrollBar.Slider.Handler.SetHeight((int)size_handler);
            }
            //step of slider
            float step_count = (float)total_invisible_size / (float)_area.GetScrollYStep();
            VScrollBar.Slider.SetStep((VScrollBar.Slider.GetMaxValue() - VScrollBar.Slider.GetMinValue()) / step_count);
            VScrollBar.Slider.SetCurrentValue((100.0f / total_invisible_size) * Math.Abs(_area.GetScrollYOffset()));
        }
        private void UpdateHorizontalSlider()//horizontal slider
        {
            int visible_area = _area.GetWidth() - _area.GetPadding().Left - _area.GetPadding().Right - 2 * _area.GetCursorWidth();
            int total = _area.GetTextWidth();

            int total_invisible_size = total - visible_area;
            if (total <= visible_area)
            {
                HScrollBar.Slider.Handler.SetWidth(0);
                HScrollBar.Slider.SetStep(HScrollBar.Slider.GetMaxValue());
                h_size = 0;
                HScrollBar.Slider.SetCurrentValue(0);
                if (GetHScrollBarVisible() == ScrollBarVisibility.AsNeeded)
                {
                    HScrollBar.SetDrawable(false);
                    Menu.SetVisible(false);
                    _grid.UpdateLayout();
                }
                return;
            }
            if (GetHScrollBarVisible() == ScrollBarVisibility.AsNeeded)
            {
                HScrollBar.SetDrawable(true);
                if (!VScrollBar.IsDrawable())
                    Menu.SetVisible(false);
                else
                    Menu.SetVisible(true);
                _grid.UpdateLayout();
            }
            h_size = total_invisible_size;

            if (total_invisible_size > 0)
            {
                float size_handler = (float)(visible_area)
                    / (float)total * 100.0f;
                size_handler = (float)HScrollBar.Slider.GetWidth() / 100.0f * size_handler;
                //size of handler
                HScrollBar.Slider.Handler.SetWidth((int)size_handler);
            }
            //step of slider
            float step_count = (float)total_invisible_size / (float)_area.GetScrollXStep();
            HScrollBar.Slider.SetStep((HScrollBar.Slider.GetMaxValue() - HScrollBar.Slider.GetMinValue()) / step_count);
            HScrollBar.Slider.SetCurrentValue((100.0f / total_invisible_size) * Math.Abs(_area.GetScrollXOffset()));
        }

        /// <summary>
        /// Set width of the TextArea
        /// </summary>
        public override void SetWidth(int width)
        {
            base.SetWidth(width);
            UpdateHorizontalSlider();
            HScrollBar.Slider.UpdateHandler();
            // _area.SetWidth(width);
        }

        /// <summary>
        /// Set height of the TextArea
        /// </summary>
        public override void SetHeight(int height)
        {
            base.SetHeight(height);
            UpdateVerticalSlider();
            VScrollBar.Slider.UpdateHandler();
            // _area.SetHeight(height);
        }

        private void UpdateElements()
        {
            UpdateVerticalSlider();
            VScrollBar.Slider.UpdateHandler();
            UpdateHorizontalSlider();
            HScrollBar.Slider.UpdateHandler();
        }

        /// <summary>
        /// Initialization and adding of all elements in the TextArea
        /// </summary>
        public override void InitElements()
        {
            //Adding
            base.AddItem(_grid);
            _grid.InsertItem(_area, 0, 0);
            _grid.InsertItem(VScrollBar, 0, 1);
            _grid.InsertItem(HScrollBar, 1, 0);
            _grid.InsertItem(Menu, 1, 1);

            //Events Connections
            EventScrollUp += VScrollBar.EventScrollUp.Invoke;
            EventScrollDown += VScrollBar.EventScrollDown.Invoke;
            _area.TextChanged += UpdateElements;

            VScrollBar.Slider.EventValueChanged += (sender) => { UpdateVListArea(); _area.SetFocus();};
            HScrollBar.Slider.EventValueChanged += (sender) => { UpdateHListArea(); _area.SetFocus();};

            // create menu
            _menu = new ContextMenu(GetHandler());
            _menu.SetBackground(60, 60, 60);
            _menu.SetPassEvents(false);

            MenuItem go_up = new MenuItem("Go up");
            go_up.SetForeground(Color.FromArgb(210, 210, 210));
            go_up.EventMouseClick += ((sender, args) =>
            {
                _area.SetScrollYOffset(0);
                UpdateElements();
                _area.SetFocus();
            });

            MenuItem go_down = new MenuItem("Go down");
            go_down.SetForeground(Color.FromArgb(210, 210, 210));
            go_down.EventMouseClick += ((sender, args) =>
            {
                _area.SetScrollYOffset(-_area.GetTextHeight());
                UpdateElements();
                _area.SetFocus();
            });

            MenuItem go_up_left = new MenuItem("Go up and left");
            go_up_left.SetForeground(Color.FromArgb(210, 210, 210));
            go_up_left.EventMouseClick += ((sender, args) =>
            {
                _area.SetScrollYOffset(0);
                _area.SetScrollXOffset(0);
                UpdateElements();
                _area.SetFocus();
            });

            MenuItem go_down_right = new MenuItem("Go down and right");
            go_down_right.SetForeground(Color.FromArgb(210, 210, 210));
            go_down_right.EventMouseClick += ((sender, args) =>
            {
                _area.SetScrollYOffset(-_area.GetTextHeight());
                _area.SetScrollXOffset(-_area.GetTextWidth());
                UpdateElements();
                _area.SetFocus();
            });
            _menu.AddItems(go_up_left, go_down_right, go_up, go_down);
            Menu.EventMouseClick += (sender, args) =>
            {
                if (!_is_menu_disabled)
                    _menu.Show(sender, args);
            };
            _menu.ActiveButton = MouseButton.ButtonLeft;
            _menu.SetShadow(10, 0, 0, Color.Black);

            UpdateElements();
        }

        /// <summary>
        /// Set text in the TextArea
        /// </summary>
        public void SetText(String text)
        {
            _area.SetText(text);
        }

        /// <returns> text from the TextArea </returns>
        public String GetText()
        {
            return _area.GetText();
        }

        /// <summary>
        /// Set style of the TextArea
        /// </summary>
        //style
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);
            SetForeground(style.Foreground);
            SetFont(style.Font);

            Style inner_style = style.GetInnerStyle("vscrollbar");
            if (inner_style != null)
            {
                VScrollBar.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("hscrollbar");
            if (inner_style != null)
            {
                HScrollBar.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("textedit");
            if (inner_style != null)
            {
                _area.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("menu");
            if (inner_style != null)
            {
                Menu.SetStyle(inner_style);
            }
        }

        // public override void SetInnerStyle(string element, Style style)
        // {
        //     Style inner_style = style.GetInnerStyle(element);

        // }

        /// <summary>
        /// Text color in the TextArea
        /// </summary>
        /// <overloads> Text color in the TextArea </overloads>
        public void SetForeground(Color color)
        {
            _area.SetForeground(color);
        }
        public void SetForeground(int r, int g, int b)
        {
            if (r < 0) r = Math.Abs(r); if (r > 255) r = 255;
            if (g < 0) g = Math.Abs(g); if (g > 255) g = 255;
            if (b < 0) b = Math.Abs(b); if (b > 255) b = 255;
            _area.SetForeground(Color.FromArgb(255, r, g, b));
        }
        public void SetForeground(int r, int g, int b, int a)
        {
            if (r < 0) r = Math.Abs(r); if (r > 255) r = 255;
            if (g < 0) g = Math.Abs(g); if (g > 255) g = 255;
            if (b < 0) b = Math.Abs(b); if (b > 255) b = 255;
            _area.SetForeground(Color.FromArgb(a, r, g, b));
        }
        public void SetForeground(float r, float g, float b)
        {
            if (r < 0) r = Math.Abs(r); if (r > 1.0f) r = 1.0f;
            if (g < 0) g = Math.Abs(g); if (g > 1.0f) g = 1.0f;
            if (b < 0) b = Math.Abs(b); if (b > 1.0f) b = 1.0f;
            _area.SetForeground(Color.FromArgb(255, (int)(r * 255.0f), (int)(g * 255.0f), (int)(b * 255.0f)));
        }
        public void SetForeground(float r, float g, float b, float a)
        {
            if (r < 0) r = Math.Abs(r); if (r > 1.0f) r = 1.0f;
            if (g < 0) g = Math.Abs(g); if (g > 1.0f) g = 1.0f;
            if (b < 0) b = Math.Abs(b); if (b > 1.0f) b = 1.0f;
            _area.SetForeground(Color.FromArgb((int)(a * 255.0f), (int)(r * 255.0f), (int)(g * 255.0f), (int)(b * 255.0f)));
        }
        public Color GetForeground()
        {
            return _area.GetForeground();
        }

        /// <summary>
        /// Space between lines in the TextArea
        /// </summary>
        public void SetLineSpacer(int lineSpacer)
        {
            _area.SetLineSpacer(lineSpacer);
        }
        public int GetLineSpacer()
        {
            return _area.GetLineSpacer();
        }

        /// <summary>
        /// Text margin in the TextArea
        /// </summary>
        public void SetTextMargin(Indents margin)
        {
            _area.SetTextMargin(margin);
        }
        public Indents GetTextMargin()
        {
            return _area.GetTextMargin();
        }

        /// <summary>
        /// Text font in the TextArea
        /// </summary>
        public void SetFont(Font font)
        {
            _area.SetFont(font);
        }
        public Font GetFont()
        {
            return _area.GetFont();
        }

        /// <returns> Returns width of the whole text in the TextArea
        /// (includes visible and invisible parts of the text) </returns>
        public int GetTextWidth()
        {
            return _area.GetWidth();
        }

        /// <returns> Returns height of the whole text in the TextArea
        /// (includes visible and invisible parts of the text) </returns>
        public int GetTextHeight()
        {
            return _area.GetTextHeight();
        }

        /// <summary>
        /// Set TextArea focused/unfocused
        /// </summary>
        public override void SetFocused(bool value)
        {
            base.SetFocused(value);
            _area.SetFocused(value);
        }

        /// <summary>
        /// Remove all text from the TextArea
        /// </summary>
        public void ClearArea()
        {
            _area.Clear();
        }
    }
}