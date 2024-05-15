const commonColors = {
  text: {
    primary: '#1A1A1A',
    primaryContrast: '#CCD8EB',
    primaryHeader: '#F5F5F5',
    secondary: '#444444',
    secondaryHeader: '#E8E8E8',
    tertiary: '#B6B6B6',
    tertiaryHeader: '#CCD8EB',
    error: '#D3000C',
    info: '#1C7FDB',
    success: '#007504',
    warning: '#E9A631',
  },
  background: {
    primary: '#444444',
    primaryHeader: '#8896AB',
    secondary: '#F5F5F5',
    secondaryHeader: '#CCD8EB',
    tertiary: '#F5F5F5',
    tertiaryHeader: '#CCD8EB',
    edit: '#EBF1F6',
    error: '#F6CCCE',
    info: '#D9E5EE',
    success: '#D0E4D6',
    warning: '#FAEDD5',
  },
};
const theme = {
  button: {
    color: {
      header: {
        background: commonColors.background.primaryHeader,
        backgroundDisabled: 'rgba(255, 255, 255, 0.1)',
        backgroundHovered: 'rgba(255, 255, 255, 0.1)',
        text: commonColors.text.primaryHeader,
      },
      primary: {
        background: commonColors.background.secondaryHeader,
        backgroundDisabled: '#bcbcbc',
        backgroundHovered: '#d0e0e3',
        text: commonColors.text.primary,
      },
      secondary: {
        background: 'none',
        backgroundDisabled: 'rgba(255, 255, 255, 0.1)',
        backgroundHovered: 'rgba(0, 0, 0, 0.05)',
        text: commonColors.text.primary,
      },
    },
  },
  card: {
    color: {
      background: {
        edit: commonColors.background.edit,
        error: commonColors.background.error,
        info: commonColors.background.info,
        paper: commonColors.background.secondary,
        success: commonColors.background.success,
        warning: commonColors.background.warning,
      },
    },
  },
  circularProgress: {
    color: '#1192E8',
  },
  colors: commonColors,
  header: {
    color: {
      background: commonColors.background.primaryHeader,
      text: {
        primary: commonColors.text.primaryHeader,
        secondary: commonColors.text.secondaryHeader,
      },
    },
    height: 48,
  },
  hover: {
    background: 'rgba(0, 0, 0, 0.05)',
    backgroundLight: 'rgba(255, 255, 255, 0.15)',
    selected: {
      background: 'rgba(0, 0, 0, 0.10)',
    },
  },
  icon: {
    color: {
      default: commonColors.text.tertiary,
      header: commonColors.text.tertiaryHeader,
      error: commonColors.text.error,
      success: commonColors.text.success,
      warning: commonColors.text.warning,
      info: commonColors.text.info,
    },
  },
  iconButton: {
    border: {
      color: 'rgba(255, 255, 255, 0.5)',
    },
  },
  input: {
    color: {
      header: {
        border: '',
        placeholder: '',
        text: {
          primary: '',
          secondary: '',
        },
      },
      primary: {
        border: commonColors.text.tertiary,
        placeholder: commonColors.text.tertiary,
        text: {
          primary: commonColors.text.primary,
          secondary: commonColors.text.secondary,
        },
      },
    }
  },
  link: {
    color: '#1C7FDB',
  },
  loading: {
    background: '#FFFFFF',
  },
  menu: {
    color: {
      header: {
        background: commonColors.background.secondary,
      },
      primary: {
        background: commonColors.background.secondary,
      },
    }
  },
  menuItem: {
    color: {
      header: {
        backgroundHovered: 'rgba(0, 0, 0, 0.05)',
        backgroundSelected: 'rgba(0, 0, 0, 0.1)',
      },
      primary: {
        backgroundHovered: 'rgba(0, 0, 0, 0.05)',
        backgroundSelected: 'rgba(0, 0, 0, 0.1)',
      },
    }
  },
  pageContainer: {
    border: '#E6E6E6',
    color: {
      background: commonColors.background.primary,
      text: {
        primary: '',
        secondary: '',
      },
    },
    content: {
      color: {
        background: commonColors.background.secondary,
      },
      width: 1024,
    },
  },
  sideBar: {
    background: '#F1F1F1',
    border: '#E6E6E6',
    width: 220,
  },
  spacing: (x = 1) => x * 8,
  typography: {
    color: {
      error: commonColors.text.error,
      info: commonColors.text.info,
      primary: commonColors.text.primary,
      secondary: commonColors.text.secondary,
      tertiary: commonColors.text.tertiary,
      success: commonColors.text.success,
      warning: commonColors.text.warning,
    },
    variants: {
      caption: {
        fontFamily: 'sans-serif',
        fontSize: '12px',
        fontWeight: 400,
        letterSpacing: '0.03333em',
        lineHeight: 1.3,
      },
      default: {
        fontFamily: 'sans-serif',
        fontSize: '14px',
        fontWeight: 400,
        letterSpacing: '0.03333em',
        lineHeight: 1.3,
      },
      subtitle: {
        fontFamily: 'sans-serif',
        fontSize: '16px',
        fontWeight: 400,
        letterSpacing: '0.03333em',
        lineHeight: 1.3,
      },
      title: {
        fontFamily: 'sans-serif',
        fontSize: '20px',
        fontWeight: 400,
        letterSpacing: '0.03333em',
        lineHeight: 1.3,
      },
    },
  },
};

export default theme;
