﻿using System.Drawing;

namespace SpaceVIL
{
    public class Frame : VisualItem
    {
        static int count = 0;
        public Frame()
        {
            SetItemName("Frame_" + count);
            SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            count++;
        }

        protected internal override bool GetHoverVerification(float xpos, float ypos)
        {
            return false;
        }
    }
}
