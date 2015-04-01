package sfogl2;

import android.opengl.GLES20;

import shadow.math.SFVertex4f;

public class SFOGLStateEngine {

    public static SFOGLState glClear(final int mask) {
        return new SFOGLState() {
            @Override
            public void applyState() {
                GLES20.glClear(mask);
            }
        };
    }

    public static SFOGLState glBlendColor(final SFVertex4f color) {
        return new SFOGLState() {
            @Override
            public void applyState() {
                GLES20.glBlendColor(color.getX(), color.getY(), color.getZ(), color.getW());
            }
        };
    }

    public static SFOGLState glBlendFunc(final int srcFunc, final int destFunc) {
        return new SFOGLState() {
            @Override
            public void applyState() {
                GLES20.glBlendFunc(srcFunc, destFunc);
            }
        };
    }

    public static SFOGLState glBlendFuncSeparate(final int srcFunc, final int destFunc, final int srcFuncAlpha, final int destFuncAlpha) {
        return new SFOGLState() {
            @Override
            public void applyState() {
                GLES20.glBlendFuncSeparate(srcFunc, destFunc, srcFuncAlpha, destFuncAlpha);
            }
        };
    }

    public static SFOGLState glBlendEquation(final int mode) {
        return new SFOGLState() {
            @Override
            public void applyState() {
                GLES20.glBlendEquation(mode);
            }
        };
    }

    public static SFOGLState glBlendEquationSeparate(final int mode, final int alphaMode) {
        return new SFOGLState() {
            @Override
            public void applyState() {
                GLES20.glBlendEquationSeparate(mode, alphaMode);
            }
        };
    }

    public static SFOGLState glClearColor(final SFVertex4f c) {
        return new SFOGLState() {
            @Override
            public void applyState() {
                GLES20.glClearColor(c.getX(), c.getY(), c.getZ(), c.getW());
            }
        };
    }

    public static SFOGLState glClearDepth(final double depth) {
        return new SFOGLState() {
            @Override
            public void applyState() {
                GLES20.glClearDepthf((float) depth);
            }
        };
    }

    public static SFOGLState glClearStencil(final int stencil) {
        return new SFOGLState() {
            @Override
            public void applyState() {
                GLES20.glClearStencil(stencil);
            }
        };
    }

    public static SFOGLState glColorMask(final boolean red, final boolean green, final boolean blue, final boolean alpha) {
        return new SFOGLState() {
            @Override
            public void applyState() {
                GLES20.glColorMask(red, green, blue, alpha);
            }
        };
    }

    public static SFOGLState glDepthFunc(final int func) {
        return new SFOGLState() {
            @Override
            public void applyState() {
                GLES20.glDepthFunc(func);
            }
        };
    }

    public static SFOGLState glDepthMask(final boolean depth) {
        return new SFOGLState() {
            @Override
            public void applyState() {
                GLES20.glDepthMask(depth);
            }
        };
    }

    public static SFOGLState glEnable(final int function) {
        return new SFOGLState() {
            @Override
            public void applyState() {
                GLES20.glEnable(function);
            }
        };
    }

    public static SFOGLState glDisable(final int function) {
        return new SFOGLState() {
            @Override
            public void applyState() {
                GLES20.glDisable(function);
            }
        };
    }

    public static SFOGLState sfoglFacingState(final SFOGLFacingState state) {
        return new SFOGLState() {
            @Override
            public void applyState() {
                int cullFace = GLES20.GL_NONE;
                int frontFace = GLES20.GL_CCW;
                switch (state) {
                    case CULL_CCW:
                        cullFace = GLES20.GL_FRONT;
                        break;
                    case CULL_CW:
                        cullFace = GLES20.GL_BACK;
                        break;
                    case FRONT_CW:
                        frontFace = GLES20.GL_CW;
                        break;
                    default:
                }
                GLES20.glFrontFace(frontFace);
                GLES20.glCullFace(cullFace);
            }
        };
    }

    public static SFOGLState glSampleCoverage(final float value, final boolean invert) {
        return new SFOGLState() {
            @Override
            public void applyState() {
                GLES20.glSampleCoverage(value, invert);
            }
        };
    }

    public static SFOGLState glStencilOpSeparate(final int face, final int fail, final int zfail, final int zpass) {
        return new SFOGLState() {
            @Override
            public void applyState() {
                GLES20.glStencilOpSeparate(face, fail, zfail, zpass);
            }
        };
    }

    public static SFOGLState glStencilMaskSeparate(final int face, final int stencilMask) {
        return new SFOGLState() {
            @Override
            public void applyState() {
                GLES20.glStencilMaskSeparate(face, stencilMask);
            }
        };
    }

    public static SFOGLState glStencilFuncSeparate(final int face, final int func, final int ref, final int mask) {
        return new SFOGLState() {
            @Override
            public void applyState() {
                GLES20.glStencilFuncSeparate(face, func, ref, mask);
            }
        };
    }

    public static SFOGLState glDepthRange(final float zNear, final float zFar) {
        return new SFOGLState() {
            @Override
            public void applyState() {
                GLES20.glDepthRangef(zNear, zFar);
            }
        };
    }

    public static SFOGLState glScissor(final int x, final int y, final int width, final int height) {
        return new SFOGLState() {
            @Override
            public void applyState() {
                GLES20.glScissor(x, y, width, height);
            }
        };
    }

    public enum SFOGLFacingState {
        /**
         * Does Not apply Face Culling. Front faces are CounterClockWise, Back Faces are ClockWise
         */
        FRONT_CCW,
        /**
         * Does Not apply Face Culling. Front faces are ClockWise, Back Faces are CounterClockWise
         */
        FRONT_CW,
        /**
         * Apply Face Culling on ClockWise Faces. Front faces are CounterClockWise.
         */
        CULL_CW,
        /**
         * Apply Face Culling on CounterClockWise Faces. Front faces are ClockWise.
         */
        CULL_CCW
    }
}
