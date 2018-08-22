using System;
using System.Collections.Generic;
using System.Drawing;

namespace SpaceVIL
{
    public class ComboBox : VisualItem
    {
        static int count = 0;
        public ButtonCore _selected = new ButtonCore();
        public ButtonCore _dropdown = new ButtonCore();
        public CustomShape _arrow = new CustomShape();
        public ComboBoxDropDown _dropdownarea = new ComboBoxDropDown();
        public EventCommonMethod SelectionChanged;

        public ComboBox()
        {
            SetBackground(Color.Transparent);
            SetItemName("ComboBox_" + count);
            count++;

            EventKeyPress += OnKeyPress;
            EventMousePressed += (sender, args) => ShowDropDownList();
        }

        protected virtual void OnKeyPress(object sender, KeyArgs args)
        {
            if (args.Scancode == 0x1C)
                EventMouseClick?.Invoke(this, new MouseArgs());
        }

        //text init
        public void SetTextAlignment(ItemAlignment alignment)
        {
            _selected.SetTextAlignment(alignment);
        }
        public void SetTextMargin(Margin margin)
        {
            _selected.SetMargin(margin);
        }
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
        public void SetText(String text)
        {
            _selected.SetText(text);
        }
        public String GetText()
        {
            return _selected.GetText();
        }
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

        public override void InitElements()
        {
            //selected
            _selected.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            _selected.SetAlignment(ItemAlignment.Right | ItemAlignment.VCenter);
            _selected.SetForeground(Color.Black);
            _selected.SetMargin(0, 0, 20, 0);
            _selected.SetPadding(10, 0, 0, 0);
            _selected.SetBackground(220, 220, 220);

            //dropdown
            _dropdown.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
            _dropdown.SetWidth(20);
            _dropdown.SetAlignment(ItemAlignment.Right | ItemAlignment.VCenter);
            _dropdown.SetBackground(255, 181, 111);
            _dropdown.AddItemState(true, ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(40, 255, 255, 255)
            });

            //arrow
            _arrow.SetTriangles(GraphicsMathService.GetTriangle(a: 180));
            _arrow.SetBackground(50, 50, 50);
            _arrow.SetSize(14, 6);
            _arrow.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            _arrow.SetAlignment(ItemAlignment.HCenter | ItemAlignment.VCenter);

            //adding
            AddItems(_selected, _dropdown);

            _dropdown.AddItem(_arrow);
            _selected.SetTextAlignment(ItemAlignment.Left | ItemAlignment.VCenter);

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
            _dropdownarea.Show();
        }

        public void AddToList(BaseItem item)
        {
            _dropdownarea.Add(item);
        }
        public void RemoveFromLst(BaseItem item)
        {
            _dropdownarea.Remove(item);
        }
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
    }
}