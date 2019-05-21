using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using System.Threading;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public class PasswordLine : HorizontalStack
    {
        static int count = 0;

        private BlankItem _show_pwd_btn;
        private TextEncrypt _textEncrypt;

        /// <summary>
        /// Constructs a PasswordLine
        /// </summary>
        public PasswordLine()
        {
            SetItemName("PasswordLine_" + count);
            _show_pwd_btn = new BlankItem();
            _show_pwd_btn.SetItemName(GetItemName() + "_marker");
            _textEncrypt = new TextEncrypt();
            count++;

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.PasswordLine)));
        }

        private void ShowPassword(bool value)
        {
            _textEncrypt.ShowPassword(value);
        }

        /// <summary>
        /// Set PasswordLine focused/unfocused
        /// </summary>
        public override void SetFocus()
        {
            _textEncrypt.SetFocus();
        }

        /// <summary>
        /// Text alignment in the PasswordLine
        /// </summary>
        public void SetTextAlignment(ItemAlignment alignment)
        {
            _textEncrypt.SetTextAlignment(alignment);
        }
        public void SetTextAlignment(params ItemAlignment[] alignment)
        {
            _textEncrypt.SetTextAlignment(alignment);
        }

        /// <summary>
        /// Text margin in the PasswordLine
        /// </summary>
        public void SetTextMargin(Indents margin)
        {
            _textEncrypt.SetMargin(margin);
        }

        /// <summary>
        /// Text font in the PasswordLine
        /// </summary>
        public void SetFont(Font font)
        {
            _textEncrypt.SetFont(font);
        }
        public void SetFontSize(int size)
        {
            _textEncrypt.SetFontSize(size);
        }

        public void SetFontStyle(FontStyle style)
        {
            _textEncrypt.SetFontStyle(style);
        }

        public void SetFontFamily(FontFamily font_family)
        {
            _textEncrypt.SetFontFamily(font_family);
        }
        public Font GetFont()
        {
            return _textEncrypt.GetFont();
        }

        /// <returns> password string </returns>
        public String GetPassword()
        {
            return _textEncrypt.GetPassword();
        }

        /// <summary>
        /// Text color in the PasswordLine
        /// </summary>
        public void SetForeground(Color color)
        {
            _textEncrypt.SetForeground(color);
        }
        public void SetForeground(int r, int g, int b)
        {
            _textEncrypt.SetForeground(r, g, b);
        }
        public void SetForeground(int r, int g, int b, int a)
        {
            _textEncrypt.SetForeground(r, g, b, a);
        }
        public void SetForeground(float r, float g, float b)
        {
            _textEncrypt.SetForeground(r, g, b);
        }
        public void SetForeground(float r, float g, float b, float a)
        {
            _textEncrypt.SetForeground(r, g, b, a);
        }
        public Color GetForeground()
        {
            return _textEncrypt.GetForeground();
        }

        /// <summary>
        /// Returns if PasswordLine editable or not
        /// </summary>
        public bool IsEditable()
        {
            return _textEncrypt.IsEditable();
        }

        /// <summary>
        /// Set PasswordLine editable true or false
        /// </summary>
        public void SetEditable(bool value)
        {
            _textEncrypt.SetEditable(value);
        }

        /// <summary>
        /// Initialization and adding of all elements in the PasswordLine
        /// </summary>
        public override void InitElements()
        {
            AddItems(_textEncrypt, _show_pwd_btn);

            ImageItem eye = new ImageItem(DefaultsService.GetDefaultImage(EmbeddedImage.Eye, EmbeddedImageSize.Size32x32));
            eye.KeepAspectRatio(true);
            Color eyeBtnShadeColor = Color.FromArgb(80, 80, 80);
            eye.SetColorOverlay(eyeBtnShadeColor);
            _show_pwd_btn.AddItem(eye);

            _show_pwd_btn.SetPassEvents(false);
            _show_pwd_btn.EventMousePress += (sender, args) =>
            {
                ShowPassword(true);
                eye.SetColorOverlay(Color.FromArgb(30, 30, 30));
            };
            _show_pwd_btn.EventMouseClick += (sender, args) =>
            {
                ShowPassword(false);
                eye.SetColorOverlay(eyeBtnShadeColor);
            };
            _show_pwd_btn.EventMouseLeave += (sender, args) =>
            {
                ShowPassword(false);
                eye.SetColorOverlay(eyeBtnShadeColor);
            };
        }

        /// <summary>
        /// Returns width of the whole text in the PasswordLine
        /// (includes visible and invisible parts of the text)
        /// </summary>
        public int GetTextWidth()
        {
            return _textEncrypt.GetWidth();
        }

        /// <summary>
        /// Returns height of the whole text in the PasswordLine
        /// (includes visible and invisible parts of the text)
        /// </summary>
        public int GetTextHeight()
        {
            return _textEncrypt.GetHeight();
        }

        /// <summary>
        /// Remove all text from the PasswordLine
        /// </summary>
        public override void Clear()
        {
            _textEncrypt.Clear();
        }

        //style
        /// <summary>
        /// Set style of the PasswordLine
        /// </summary>
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);

            Style inner_style = style.GetInnerStyle("showmarker");
            if (inner_style != null)
            {
                _show_pwd_btn.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("textedit");
            if (inner_style != null)
            {
                _textEncrypt.SetStyle(inner_style);
            }
        }

        public void SetSubstrateText(String substrateText)
        {
            _textEncrypt.SetSubstrateText(substrateText);
        }

        public void SetSubstrateFontSize(int size)
        {
            _textEncrypt.SetSubstrateFontSize(size);
        }

        public void SetSubstrateFontStyle(FontStyle style)
        {
            _textEncrypt.SetSubstrateFontStyle(style);
        }

        public void SetSubstrateForeground(Color foreground)
        {
            _textEncrypt.SetSubstrateForeground(foreground);
        }

        public void SetSubstrateForeground(int r, int g, int b)
        {
            _textEncrypt.SetSubstrateForeground(r, g, b);
        }

        public void SeSubstratetForeground(int r, int g, int b, int a)
        {
            _textEncrypt.SetSubstrateForeground(r, g, b, a);
        }

        public void SetSubstrateForeground(float r, float g, float b)
        {
            _textEncrypt.SetSubstrateForeground(r, g, b);
        }

        public void SetSubstrateForeground(float r, float g, float b, float a)
        {
            _textEncrypt.SetSubstrateForeground(r, g, b, a);
        }

        public Color getSubstrateForeground()
        {
            return _textEncrypt.GetSubstrateForeground();
        }

        public String getSubstrateText()
        {
            return _textEncrypt.GetSubstrateText();
        }
    }
}