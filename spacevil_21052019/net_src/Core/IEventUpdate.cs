﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL.Core
{
    public interface IEventUpdate
    {
        void Update(GeometryEventType type, int value);
    }
}
