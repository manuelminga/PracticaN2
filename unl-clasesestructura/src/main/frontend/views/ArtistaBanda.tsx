import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { Button, ComboBox, DatePicker, Dialog, Grid, GridColumn, GridItemModel, GridSortColumn, HorizontalLayout, Icon, Select, TextArea, TextField, VerticalLayout } from '@vaadin/react-components';
import { Notification } from '@vaadin/react-components/Notification';

import { useSignal } from '@vaadin/hilla-react-signals';
import handleError from 'Frontend/views/_ErrorHandler';
import { Group, ViewToolbar } from 'Frontend/components/ViewToolbar';

import { useDataProvider } from '@vaadin/hilla-react-crud';
import Banda from 'Frontend/generated/com/unl/clasesestructura/base/models/Cancion';
import { ArtistaBandaService, BandaService, ExpresionService } from 'Frontend/generated/endpoints';
import Expresion from 'Frontend/generated/com/unl/clasesestructura/base/models/Expresion';
import { useEffect, useState } from 'react';

export const config: ViewConfig = {
  title: 'Artista Banda',
  menu: {
    icon: 'vaadin:clipboard-check',
    order: 5,
    title: 'Artista Banda',
  },
};

type BandaEntryFormProps = {
  onBandaCreated?: () => void;
};

function BandaEntryForm(props: BandaEntryFormProps) {
  const dialogOpened = useSignal(false);

  const open = () => {
    dialogOpened.value = true;
  };

  const close = () => {
    dialogOpened.value = false;
  };

  const expresion = useSignal('');


  const createBanda = async () => {
    try {
      if (expresion.value.trim().length > 0) {
        await ExpresionService.create(expresion.value);
        if (props.onBandaCreated) {
          props.onBandaCreated();
        }
        expresion.value = '';

        dialogOpened.value = false;
        Notification.show('Banda creada exitosamente', { duration: 5000, position: 'bottom-end', theme: 'success' });
      } else {
        Notification.show('No se pudo crear, faltan datos', { duration: 5000, position: 'top-center', theme: 'error' });
      }

    } catch (error) {
      console.log(error);
      handleError(error);
    }
  };

  return (
    <>
      <Dialog
        aria-label="Registrar Banda"
        draggable
        modeless
        opened={dialogOpened.value}
        onOpenedChanged={(event) => {
          dialogOpened.value = event.detail.value;
        }}
        header={
          <h2
            className="draggable"
            style={{
              flex: 1,
              cursor: 'move',
              margin: 0,
              fontSize: '1.5em',
              fontWeight: 'bold',
              padding: 'var(--lumo-space-m) 0',
            }}
          >
            Registrar Banda
          </h2>
        }
        footerRenderer={() => (
          <>
            <Button onClick={close}>Cancelar</Button>
            <Button theme="primary" onClick={createBanda}>
              Registrar
            </Button>
          </>
        )}
      >
        <VerticalLayout
          theme="spacing"
          style={{ width: '300px', maxWidth: '100%', alignItems: 'stretch' }}
        >
          <VerticalLayout style={{ alignItems: 'stretch' }}>
            <TextField label="Nombre"
              placeholder='Ingrese el nombre de la banda'
              aria-label='Ingrese el nombre de la banda'
              value={expresion.value}
              onValueChanged={(evt) => (expresion.value = evt.detail.value)}
            />

          </VerticalLayout>
        </VerticalLayout>
      </Dialog>
      <Button onClick={open}>Registrar</Button>
    </>
  );
}

const dateFormatter = new Intl.DateTimeFormat(undefined, {
  dateStyle: 'medium',
});

function link({ item }: { item: Banda }) {
  return (
    <span>
      <Button>
        Editar
      </Button>
    </span>
  );
}

function view_valid({ item }: { item: Expresion }) {
  return (
    <span>
      {item.isCorrecto ? "Verdadero" : "Falso"}
    </span>
  );
}

function index({ model }: { model: GridItemModel<Expresion> }) {
  return (
    <span>
      {model.index + 1}
    </span>
  );
}

export default function ArtistaBandaListView() {
  const [items, setItems] = useState([]);
  useEffect(() => {
    ArtistaBandaService.listAll().then(function (data) {
      //items.values = data;
      setItems(data);
    });
  }, []);

  const order = (event, columnId) => {
    console.log(event);
    const direction = event.detail.value;
    // Custom logic based on the sorting direction
    console.log(`Sort direction changed for column ${columnId} to ${direction}`);

    var dir = (direction == 'asc') ? 1 : 2;
    ArtistaBandaService.order(columnId, dir).then(function (data) {
      setItems(data);
    });
  }

  /*const dataProvider = useDataProvider({
    list: () => ArtistaBandaServices.listAll(),
  });*/
  //para buscar
  const criterio = useSignal('');
  const texto = useSignal('');
  const itemSelect = [
    {
      label: 'Banda',
      value: 'banda',
    },
    {
      label: 'Artista',
      value: 'artista',
    },
  ];
  const search = async () => {
    
    try {
      console.log(criterio.value+" "+texto.value);
      ArtistaBandaService.search(criterio.value, texto.value, 0).then(function (data) {
        setItems(data);
      });

      criterio.value = '';
      texto.value = '';

      Notification.show('Busqueda realizada', { duration: 5000, position: 'bottom-end', theme: 'success' });


    } catch (error) {
      console.log(error);
      handleError(error);
    }
  };
  return (
    <main className="w-full h-full flex flex-col box-border gap-s p-m">
      <ViewToolbar title="Bandas">
        <Group>
          <BandaEntryForm />
        </Group>
      </ViewToolbar>
      <HorizontalLayout theme="spacing">
        <Select items={itemSelect}
          value={criterio.value}
          onValueChanged={(evt) => (criterio.value = evt.detail.value)}
          placeholder="Selecione un cirterio">


        </Select>

        <TextField
          placeholder="Search"
          style={{ width: '50%' }}
          value={texto.value}
          onValueChanged={(evt) => (texto.value = evt.detail.value)}
        >
          <Icon slot="prefix" icon="vaadin:search" />
        </TextField>
        <Button onClick={search} theme="primary">
          BUSCAR
        </Button>
      </HorizontalLayout>
      <Grid items={items}>
        <GridColumn header="Nro" renderer={index} />
        <GridSortColumn onDirectionChanged={(e) => order(e, "artista")} path="artista" header="Artista" />
        <GridSortColumn path="banda" header="Banda" onDirectionChanged={(e) => order(e, "banda")} />
        <GridSortColumn path="rol" header="Rol" onDirectionChanged={(e) => order(e, "rol")} />


        <GridColumn header="Acciones" renderer={link} />
      </Grid>
    </main>
  );
}