﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using SpaceVIL.Core;
using SpaceVIL.Common;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public class Label : Prototype
    {
        static int count = 0;

        private TextLine _text_object;

        /// <summary>
        /// Constructs a Label
        /// </summary>
        public Label()
        {
            SetItemName("Label_" + count);
            count++;
            _text_object = new TextLine();
            IsFocusable = false;

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.Label)));
        }

        /// <summary>
        /// Constructs a Label with text
        /// </summary>
        public Label(String text = "") : this()
        {
            SetText(text);
        }

        /// <summary>
        /// Text alignment in the Label
        /// </summary>
        public void SetTextAlignment(ItemAlignment alignment)
        {
            _text_object.SetTextAlignment(alignment);
        }

        /// <summary>
        /// Text margin in the Label
        /// </summary>
        public void SetTextMargin(Indents margin)
        {
            _text_object.SetMargin(margin);
        }

        /// <summary>
        /// Text font parameters in the Label
        /// </summary>
        public void SetFont(Font font)
        {
            _text_object.SetFont(font);
        }
        public void SetFontSize(int size)
        {
            _text_object.SetFontSize(size);
        }
        public void SetFontStyle(FontStyle style)
        {
            _text_object.SetFontStyle(style);
        }
        public void SetFontFamily(FontFamily font_family)
        {
            _text_object.SetFontFamily(font_family);
        }
        public Font GetFont()
        {
            return _text_object.GetFont();
        }

        /// <summary>
        /// Set text in the Label
        /// </summary>
        public void SetText(String text)
        {
            _text_object.SetItemText(text);
        }
        public String GetText()
        {
            return _text_object.GetItemText();
        }

        /// <summary>
        /// Text color in the Label
        /// </summary>
        public void SetForeground(Color color)
        {
            _text_object.SetForeground(color);
        }
        public void SetForeground(int r, int g, int b)
        {
            _text_object.SetForeground(r, g, b);
        }
        public void SetForeground(int r, int g, int b, int a)
        {
            _text_object.SetForeground(r, g, b, a);
        }
        public void SetForeground(float r, float g, float b)
        {
            _text_object.SetForeground(r, g, b);
        }
        public void SetForeground(float r, float g, float b, float a)
        {
            _text_object.SetForeground(r, g, b, a);
        }
        public Color GetForeground()
        {
            return _text_object.GetForeground();
        }

        /// <summary>
        /// Initialization and adding of all elements in the Label
        /// </summary>
        public override void InitElements()
        {
            //text
            //_text_object.SetAlignment(ItemAlignment.Left | ItemAlignment.VCenter);

            //aligment
            ////SetTextAlignment(ItemAlignment.Left | ItemAlignment.VCenter);

            //adding
            AddItem(_text_object);

            //update text data
            //_text_object.UpdateData(UpdateType.Critical);
        }

        /// <summary>
        /// Text width in the Label
        /// </summary>
        public int GetTextWidth()
        {
            return _text_object.GetWidth();
        }

        /// <summary>
        /// Text height in the Label
        /// </summary>
        public int GetTextHeight()
        {
            return _text_object.GetHeight();
        }

        //style
        /// <summary>
        /// Set style of the Label
        /// </summary>
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);
            
            SetFont(style.Font);
            SetForeground(style.Foreground);
            SetTextAlignment(style.TextAlignment);
        }
    }
}
