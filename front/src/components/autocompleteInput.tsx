import * as React from 'react';
import useAutocomplete, {
  AutocompleteGetTagProps,
} from '@mui/material/useAutocomplete';
import CheckIcon from '@mui/icons-material/Check';
import CloseIcon from '@mui/icons-material/Close';
import { styled } from '@mui/material/styles';
import { autocompleteClasses } from '@mui/material/Autocomplete';

const Root = styled('div')(({ theme }) => ({
  color: 'rgba(0,0,0,0.85)',
  fontSize: '14px',
  position: 'relative',
  ...theme.applyStyles('dark', {
    color: 'rgba(255,255,255,0.65)',
  }),
}));

const Label = styled('label')`
  padding: 0 0 4px;
  line-height: 1.5;
  display: block;
`;

const InputWrapper = styled('div')(({ theme }) => ({
  width: '300px',
  border: '2px solid #484848',
  backgroundColor: '#fff',
  borderRadius: '16px',
  padding: '3px',
  display: 'flex',
  flexWrap: 'wrap',
  ...theme.applyStyles('dark', {
    borderColor: '#FFC100',
    backgroundColor: '#141414',
  }),
  '&.focused': {
    borderColor: '#FFC100',
    ...theme.applyStyles('dark', {
      borderColor: '#FFC100',
    }),
  },
  '& input': {
    backgroundColor: 'transparent',
    color: 'rgba(0,0,0,.85)',
    height: '30px',
    boxSizing: 'border-box',
    padding: '4px 6px',
    width: '0',
    minWidth: '50px',
    flexGrow: 1,
    border: 0,
    margin: 0,
    outline: 0,
    ...theme.applyStyles('dark', {
      color: 'rgba(255,255,255,0.65)',
      backgroundColor: '#141414',
    }),
  },
}));

interface TagProps extends ReturnType<AutocompleteGetTagProps> {
  label: string;
}

function Tag(props: TagProps) {
  const { label, onDelete, ...other } = props;
  return (
    <div {...other}>
      <span>{label}</span>
      <CloseIcon onClick={onDelete} />
    </div>
  );
}

const StyledTag = styled(Tag)<TagProps>(({ theme }) => ({
  display: 'flex',
  alignItems: 'center',
  height: '24px',
  margin: '2px',
  lineHeight: '22px',
  backgroundColor: '#fafafa',
  border: `1px solid #484848`,
  borderRadius: '10px',
  boxSizing: 'content-box',
  padding: '0 4px 0 10px',
  outline: 0,
  overflow: 'hidden',
  ...theme.applyStyles('dark', {
    backgroundColor: 'rgba(255,255,255,0.08)',
    borderColor: '#303030',
  }),
  '&:focus': {
    borderColor: '#40a9ff',
    backgroundColor: '#e6f7ff',
    ...theme.applyStyles('dark', {
      backgroundColor: '#003b57',
      borderColor: '#177ddc',
    }),
  },
  '& span': {
    overflow: 'hidden',
    whiteSpace: 'nowrap',
    textOverflow: 'ellipsis',
  },
  '& svg': {
    fontSize: '24px',
    cursor: 'pointer',
    padding: '4px',
  },
}));

const Listbox = styled('ul')(({ theme }) => ({
  width: '300px',
  margin: '2px 0 0',
  padding: 0,
  position: 'absolute',
  listStyle: 'none',
  backgroundColor: '#fff',
  overflow: 'auto',
  maxHeight: '250px',
  borderRadius: '4px',
  boxShadow: '0 2px 8px rgb(0 0 0 / 0.15)',
  zIndex: 3,
  '&[data-placement="top"]': {
    bottom: '100%',
    top: 'auto',
    margin: '0 0 2px 0'
  },
  '&[data-placement="bottom"]': {
    top: '100%',
    bottom: 'auto',
    margin: '2px 0 0 0'
  },
  ...theme.applyStyles('dark', {
    backgroundColor: '#141414',
  }),
  '& li': {
    padding: '5px 12px',
    display: 'flex',
    '& span': {
      flexGrow: 1,
    },
    '& svg': {
      color: 'transparent',
    },
  },
  "& li[aria-selected='true']": {
    backgroundColor: '#fafafa',
    fontWeight: 600,
    ...theme.applyStyles('dark', {
      backgroundColor: '#2b2b2b',
    }),
    '& svg': {
      color: '#FFC100',
    },
  },
  [`& li.${autocompleteClasses.focused}`]: {
    backgroundColor: '#e6f7ff',
    cursor: 'pointer',
    ...theme.applyStyles('dark', {
      backgroundColor: '#003b57',
    }),
    '& svg': {
      color: 'currentColor',
    },
  },
}));
export default function CustomizedHook({ arr, value, onChange }: { arr: { id: number; name: string; }[], value: { id: number; name: string; }[], onChange: (val: { id: number; name: string; }[]) => void }) {
  const [anchorEl, setAnchorEl] = React.useState(null);
  
  const {
    getRootProps,
    getInputLabelProps,
    getInputProps,
    getTagProps,
    getListboxProps,
    getOptionProps,
    groupedOptions,
    focused,
  } = useAutocomplete({
    id: 'customized-hook',
    multiple: true,
    options: arr,
    getOptionLabel: (option) => option.name,
    value,
    onChange: (_event, newValue) => onChange(newValue),
  });

  return (
    <Root>
      <div {...getRootProps()}>
        <InputWrapper 
          ref={(node) => {
            setAnchorEl(node);
          }} 
          className={focused ? 'focused' : ''}
        >
          {value.map((v: {id : number; name: string}) => v.name).map((option: string, index: number) => {
            const { key, ...tagProps } = getTagProps({ index });
            return <StyledTag key={key} {...tagProps} label={option} />;
          })}
          <input {...getInputProps()} />
        </InputWrapper>
      </div>
      {groupedOptions.length > 0 ? (
        <Listbox {...getListboxProps()}
              data-placement={
          window.innerHeight - anchorEl?.getBoundingClientRect().bottom < 250 ? 'top' : 'bottom'
        }>
          {groupedOptions.map((option, index) => {
            const { key, ...optionProps } = getOptionProps({ option, index });
            return (
              <li key={key} {...optionProps}>
                <span>{option.name}</span>
                <CheckIcon fontSize="small" />
              </li>
            );
          })}
        </Listbox>
      ) : null}
    </Root>
  );
}
