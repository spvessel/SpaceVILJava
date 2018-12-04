using System;
using System.Collections.Generic;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public class ComboBox : Prototype
    {
        // Queue<IBaseItem> _queue = new Queue<IBaseItem>();

        static int count = 0;
        public ButtonCore _selected = new ButtonCore();
        public ButtonCore _dropdown = new ButtonCore();
        public CustomShape _arrow = new CustomShape();
        public ComboBoxDropDown _dropdownarea = new ComboBoxDropDown();
        public EventCommonMethod SelectionChanged;

        /// <summary>
        /// Constructs a ComboBox
        /// </summary>
        public ComboBox()
        {
            SetBackground(Color.Transparent);
            SetItemName("ComboBox_" + count);
            count++;

            EventKeyPress += OnKeyPress;
            EventMousePress += (sender, args) => ShowDropDownList();

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.ComboBox)));
        }
        
        void OnKeyPress(object sender, KeyArgs args)
        {
            if (args.Key == KeyCode.Enter)
                EventMouseClick?.Invoke(this, new MouseArgs());
        }

        //text init
        /// <summary>
        /// Text alignment in the ComboBox
        /// </summary>
        public void SetTextAlignment(ItemAlignment alignment)
        {
            _selected.SetTextAlignment(alignment);
        }

        /// <summary>
        /// Text margin in the ComboBox
        /// </summary>
        public void SetTextMargin(Indents margin)
        {
            _selected.SetMargin(margin);
        }

        /// <summary>
        /// Text font parameters in the ComboBox
        /// </summary>
        public void SetFont(Font font)
        {
            _selected.SetFont(font);
        }
        public void SetFontSize(int size)
        {
            _selected.SetFontSize(size);
        }
        public void SetFontStyle(FontStyle style)
        {
            _selected.SetFontStyle(style);
        }
        public void SetFontFamily(FontFamily font_family)
        {
            _selected.SetFontFamily(font_family);
        }
        public Font GetFont()
        {
            return _selected.GetFont();
        }

        /// <summary>
        /// Set text in the ComboBox
        /// </summary>
        public void SetText(String text)
        {
            _selected.SetText(text);
        }
        public String GetText()
        {
            return _selected.GetText();
        }

        /// <summary>
        /// Text color in the ComboBox
        /// </summary>
        public void SetForeground(Color color)
        {
            _selected.SetForeground(color);
        }
        public void SetForeground(int r, int g, int b)
        {
            _selected.SetForeground(r, g, b);
        }
        public void SetForeground(int r, int g, int b, int a)
        {
            _selected.SetForeground(r, g, b, a);
        }
        public void SetForeground(float r, float g, float b)
        {
            _selected.SetForeground(r, g, b);
        }
        public void SetForeground(float r, float g, float b, float a)
        {
            _selected.SetForeground(r, g, b, a);
        }
        public Color GetForeground()
        {
            return _selected.GetForeground();
        }

        /// <summary>
        /// Initialization and adding of all elements in the ComboBox
        /// </summary>
        public override void InitElements()
        {
            //adding
            AddItems(_selected, _dropdown);

            _dropdown.AddItem(_arrow);
            // _selected.SetTextAlignment(ItemAlignment.Left | ItemAlignment.VCenter);

            //dropdownarea
            _dropdownarea.Selection = _selected;
            _dropdownarea.SelectionChanged += OnSelectionChanged;
        }

        private void ShowDropDownList()
        {
            //dropdownarea
            _dropdownarea.Handler.SetWidth(_selected.GetWidth());
            _dropdownarea.Handler.SetHeight(100);

            _dropdownarea.Handler.SetX(GetHandler().GetX() + _selected.GetX());
            _dropdownarea.Handler.SetY(GetHandler().GetY() + _selected.GetY() + _selected.GetHeight());

            // Console.WriteLine(_dropdownarea.Handler.GetX() + " " + _dropdownarea.Handler.GetY());

            _dropdownarea.Show();
        }

        /// <summary>
        /// Add item to ComboBox list
        /// </summary>
        public void AddToList(IBaseItem item)
        {
            _dropdownarea.Add(item);
            // _queue.Enqueue(item);
        }

        /// <summary>
        /// Remove item from the ComboBox list
        /// </summary>
        public void RemoveFromLst(IBaseItem item)
        {
            _dropdownarea.Remove(item);
        }

        /// <summary>
        /// Current element in the ComboBox by index
        /// </summary>
        public void SetCurrentIndex(int index)
        {
            _dropdownarea.SetCurrentIndex(index);
        }
        public int GetCurrentIndex()
        {
            return _dropdownarea.GetCurrentIndex();
        }
        private void OnSelectionChanged()
        {
            SelectionChanged?.Invoke();
        }

        /// <summary>
        /// Set style of the ComboBox
        /// </summary>
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;

            base.SetStyle(style);
            SetForeground(style.Foreground);
            SetFont(style.Font);

            Style inner_style = style.GetInnerStyle("selection");
            if (inner_style != null)
            {
                _selected.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("dropdownbutton");
            if (inner_style != null)
            {
                _dropdown.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("arrow");
            if (inner_style != null)
            {
                _arrow.SetStyle(inner_style);
            }
        }
    }
}
 