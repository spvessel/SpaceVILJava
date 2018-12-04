﻿using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    public class SpinItem : Prototype
    {
        static int count = 0;
        private HorizontalStack _horzStack = new HorizontalStack();
        private VerticalStack _vertStack = new VerticalStack();
        public ButtonCore UpButton = new ButtonCore();
        public ButtonCore DownButton = new ButtonCore();
        internal TextEditRestricted textInput = new TextEditRestricted();

        /// <summary>
        /// Constructs a SpinItem
        /// </summary>
        public SpinItem()
        {
            SetItemName("SpinItem_" + count);
            count++;
            _horzStack.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            textInput.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.SpinItem)));
            UpButton.IsFocusable = false;
            DownButton.IsFocusable = false;

            UpButton.EventMouseClick += OnUpClick;
            DownButton.EventMouseClick += OnDownClick;

            EventScrollUp = OnUpClick;
            EventScrollDown = OnDownClick;
        }

        void OnUpClick(Object sender, MouseArgs args)
        {
            textInput.IncreaseValue();
        }

        void OnDownClick(Object sender, MouseArgs args)
        {
            textInput.DecreaseValue();
        }

        /// <summary>
        /// Set SpinItem's parameters
        /// </summary>
        /// <param name="currentValue"> SpinItem current value </param>
        /// <param name="minValue"> minimum available value </param>
        /// <param name="maxValue"> maximum available value </param>
        /// <param name="step"> SpinItem step </param>
        public void SetParameters(int currentValue, int minValue, int maxValue, int step)
        {
            textInput.SetParameters(currentValue, minValue, maxValue, step);
        }
        public void SetParameters(double currentValue, double minValue, double maxValue, double step)
        {
            textInput.SetParameters(currentValue, minValue, maxValue, step);
        }

        /// <summary>
        /// Values accuracy (decimal places)
        /// </summary>
        public void SetAccuracy(int accuracy)
        {
            textInput.SetAccuracy(accuracy);
        }

        /// <summary>
        /// Initialization and adding of all elements in the SpinItem
        /// </summary>
        public override void InitElements()
        {
            AddItem(_horzStack);
            _horzStack.AddItems(textInput, _vertStack);
            _vertStack.AddItems(UpButton, DownButton);
        }

        /// <summary>
        /// Set style of the SpinItem
        /// </summary>
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);

            Style inner_style = style.GetInnerStyle("buttonsarea");
            if (inner_style != null)
            {
                _vertStack.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("uparrow");
            if (inner_style != null)
            {
                UpButton.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("downarrow");
            if (inner_style != null)
            {
                DownButton.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("textedit");
            if (inner_style != null)
            {
                textInput.SetStyle(inner_style);
            }
        }
    }
}
